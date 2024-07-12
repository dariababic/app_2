package org.unizd.rma.babic

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.Button

import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.unizd.rma.babic.adapter.TaskViewAdapter
import org.unizd.rma.babic.databinding.ActivityMainBinding
import org.unizd.rma.babic.models.Task
import org.unizd.rma.babic.utils.Status
import org.unizd.rma.babic.utils.clearEditText
import org.unizd.rma.babic.utils.longToastShow
import org.unizd.rma.babic.utils.setupDialog
import org.unizd.rma.babic.utils.validateEditText
import org.unizd.rma.babic.viewmodels.TaskViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class MainActivity : AppCompatActivity() {
    // SharedPreferences reference
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var captureIV: ImageView
    private lateinit var captureIV2: ImageView
    private lateinit var imageUri: Uri
    // Declare variables for radio buttons
    private lateinit var radioButtonAsia: RadioButton
    private lateinit var radioButtonEurope: RadioButton
    private val contract = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        captureIV.setImageURI(null)
        captureIV.setImageURI(imageUri)
        captureIV2.setImageURI(null)
        captureIV2.setImageURI(imageUri)


        // Save the image URI to SharedPreferences after capturing/selecting an image
        saveImageUriToSharedPreferences(imageUri)
    }

    private val mainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    private val addTaskDialog: Dialog by lazy {
        Dialog(this).apply {
            setupDialog(R.layout.add_task_dialog)
        }
    }

    private val updateTaskDialog: Dialog by lazy {
        Dialog(this).apply {
            setupDialog(R.layout.update_task_dialog)
        }
    }


    private val loadingDialog: Dialog by lazy {
        Dialog(this).apply {
            setupDialog(R.layout.loading_dialog)
        }
    }

    private val taskViewModel: TaskViewModel by lazy {
        ViewModelProvider(this)[TaskViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        // Add task start
        val addCloseImg = addTaskDialog.findViewById<ImageView>(R.id.closeImg)
        addCloseImg.setOnClickListener { addTaskDialog.dismiss() }

        val addETTitle = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskTitle)
        val addETTitleL = addTaskDialog.findViewById<TextInputLayout>(R.id.edtaskTtileL)

        // Load imageUri from SharedPreferences
        imageUri = retrieveImageUriFromSharedPreferences() ?: createImageUri()

        addETTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETTitle, addETTitleL)
            }

        })


        val addETDesc = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskDesc)
        val addETDescL = addTaskDialog.findViewById<TextInputLayout>(R.id.edtaskDescL)

        // InputFilter to allow only numeric input for addETDesc
        addETDesc.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isDigit(source[i])) {
                    Toast.makeText(this, "Only numeric input is allowed", Toast.LENGTH_SHORT).show()
                    return@InputFilter ""
                }
            }
            null
        })

        addETDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETDesc, addETDescL)
            }
        })


        val addETSurface = addTaskDialog.findViewById<TextInputEditText>(R.id.edtaskSurface)
        val addETSurfaceL = addTaskDialog.findViewById<TextInputLayout>(R.id.edtaskSurfaceL)

        // InputFilter to allow only numeric input for addETDesc
        addETSurface.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isDigit(source[i])) {
                    Toast.makeText(this, "Only numeric input is allowed", Toast.LENGTH_SHORT).show()
                    return@InputFilter ""
                }
            }
            null
        })
        addETDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(addETSurface, addETSurfaceL)
            }
        })


        imageUri = createImageUri()
        captureIV = addTaskDialog.findViewById(R.id.selectedImageView)
        val captureImgBtn = addTaskDialog.findViewById<Button>(R.id.pickImageButton)
        captureImgBtn.setOnClickListener {

            // Update imageUri to point to the new image
            imageUri = createImageUri()
            contract.launch(imageUri)


        }


        imageUri = createImageUri()
        captureIV2 = updateTaskDialog.findViewById(R.id.upselectedImageView)
        val upcaptureBtn = updateTaskDialog.findViewById<Button>(R.id.updatepickImageButton)

        upcaptureBtn.setOnClickListener {
            imageUri = createImageUri()
            contract.launch(imageUri)


        }


///////add task dialog

        mainBinding.addTaskFABtn.setOnClickListener {
            clearEditText(addETTitle, addETTitleL)
            clearEditText(addETDesc, addETDescL)
            clearEditText(addETSurface, addETSurfaceL)


            // Reset imageUri to a new URI
            imageUri = createImageUri()
            captureIV.setImageURI(null) // Clear the ImageView
            addTaskDialog.show()
        }
        val saveTaskBtn = addTaskDialog.findViewById<Button>(R.id.saveBtn)
        saveTaskBtn.setOnClickListener {

            // Initialize radio buttons
            radioButtonAsia = addTaskDialog.findViewById(R.id.radioButtonAsia)
            radioButtonEurope = addTaskDialog.findViewById(R.id.radioButtonEurope)

            // Get the selected option from radio buttons
            val selectedCountry = if (radioButtonAsia.isChecked) {
                "Asia"
            } else {
                "Europe"
            }

            if (validateEditText(addETTitle, addETTitleL) && validateEditText(
                    addETDesc,
                    addETDescL
                ) && validateEditText(addETSurface, addETSurfaceL)
            ) {
                addTaskDialog.dismiss()

                val newTask = Task(
                    UUID.randomUUID().toString(),
                    addETTitle.text.toString().trim(),
                    addETDesc.text.toString().trim(),
                    Date(), // This line represents the date
                    addETSurface.text.toString().trim(),
                    imageUri.toString(),
                    country = selectedCountry
                    // This line represents the surface
                )

                taskViewModel.insertTask(newTask).observe(this) {

                    when (it.status) {

                        Status.LOADING -> {
                            loadingDialog.show()

                        }

                        Status.SUCCESS -> {

                            loadingDialog.dismiss()
                            if (it.data?.toInt() != -1) {
                                longToastShow("State Added Successfully")

                            }

                        }

                        Status.ERROR -> {

                            loadingDialog.dismiss()
                            it.message?.let { it1 -> longToastShow(it1) }

                        }

                    }
                }

            }
        }
        // Add task end




        // Update Task Start


        val updateETTitle = updateTaskDialog.findViewById<TextInputEditText>(R.id.updateTaskTitle)
        val updateETTitleL = updateTaskDialog.findViewById<TextInputLayout>(R.id.updatetaskTtileL)

        updateETTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(updateETTitle, updateETTitleL)
            }

        })

        val updateETDesc = updateTaskDialog.findViewById<TextInputEditText>(R.id.updateTaskDesc)
        val updateETDescL = updateTaskDialog.findViewById<TextInputLayout>(R.id.updatetaskDescL)

        // InputFilter to allow only numeric input for addETDesc
        updateETDesc.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isDigit(source[i])) {
                    Toast.makeText(this, "Only numeric input is allowed", Toast.LENGTH_SHORT).show()
                    return@InputFilter ""
                }
            }
            null
        })

        updateETDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(updateETDesc, updateETDescL)
            }
        })


        val updateSurface = updateTaskDialog.findViewById<TextInputEditText>(R.id.updateSurface)
        val updateSurfaceL = updateTaskDialog.findViewById<TextInputLayout>(R.id.updateSurfaceL)

        // InputFilter to allow only numeric input for addETDesc
        updateSurface.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isDigit(source[i])) {
                    Toast.makeText(this, "Only numeric input is allowed", Toast.LENGTH_SHORT).show()
                    return@InputFilter ""
                }
            }
            null
        })
        updateETDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable) {
                validateEditText(updateSurface, updateSurfaceL)
            }
        })

        val updateCloseImg = updateTaskDialog.findViewById<ImageView>(R.id.updatecloseImg)
        updateCloseImg.setOnClickListener { updateTaskDialog.dismiss() }

        val updateTaskBtn = updateTaskDialog.findViewById<Button>(R.id.updateBtn)


        val taskViewAdapter = TaskViewAdapter {

                type, Position, task ->

            if (type == "delete") {

                taskViewModel
//                    .deleteTAsk(task)
                    .deleteTaskUsingId(task.id).observe(this) {

                        when (it.status) {

                            Status.LOADING -> {
                                loadingDialog.show()

                            }

                            Status.SUCCESS -> {

                                loadingDialog.dismiss()
                                if (it.data != -1) {
                                    longToastShow("State Deleted Successfully")
                                }
                            }

                            Status.ERROR -> {

                                loadingDialog.dismiss()
                                it.message?.let { it1 -> longToastShow(it1) }
                            }
                        }
                    }
            } else if (type == "update") {
                // Set the image URI to the ImageView before showing the update dialog
                captureIV2.setImageURI(Uri.parse(task.imageUri))
                updateETTitle.setText(task.title)
                updateETDesc.setText(task.description)
                updateSurface.setText(task.surface)
                // Initialize radio buttons
                radioButtonAsia = updateTaskDialog.findViewById(R.id.updateradioButtonAsia)
                radioButtonEurope = updateTaskDialog.findViewById(R.id.updateradioButtonEurope)

                if (task.country == "Asia"){
                    radioButtonAsia.isChecked = true
                }else if (task.country == "Europe"){
                    radioButtonEurope.isChecked = true
                }

// Set the image URI to the updated image URI
                imageUri = Uri.parse(task.imageUri)

                updateTaskBtn.setOnClickListener {
                    // Initialize radio buttons
                    radioButtonAsia = updateTaskDialog.findViewById(R.id.updateradioButtonAsia)
                    radioButtonEurope = updateTaskDialog.findViewById(R.id.updateradioButtonEurope)

                    // Get the new image URI
                    val newImageUri = if (imageUri == null) Uri.parse(task.imageUri) else imageUri
                    // Get the selected option from radio buttons
                    val selectedCountry = if (radioButtonAsia.isChecked) {
                        "Asia"
                    } else {
                        "Europe"
                    }
                    if (validateEditText(updateETTitle, updateETTitleL) && validateEditText(
                            updateETDesc,
                            updateETDescL
                        ) && validateEditText(updateSurface, updateSurfaceL)
                    ) {

                        val updateTask = Task(
                            task.id,
                            updateETTitle.text.toString().trim(),
                            updateETDesc.text.toString().trim(),
                            Date(), // This line represents the date
                            updateSurface.text.toString().trim(),
                            newImageUri.toString(),
                            country = selectedCountry

                        )
                        updateTaskDialog.dismiss()
                        loadingDialog.show()
                        taskViewModel.
                        updateTask(updateTask)
                            .observe(this) {

                                when (it.status) {

                                    Status.LOADING -> {
                                        loadingDialog.show()

                                    }

                                    Status.SUCCESS -> {

                                        loadingDialog.dismiss()
                                        if (it.data != -1) {
                                            longToastShow("State Updated Successfully")

                                        }

                                    }

                                    Status.ERROR -> {

                                        loadingDialog.dismiss()
                                        it.message?.let { it1 -> longToastShow(it1) }

                                    }

                                }


                            }
                    }

                }

                updateTaskDialog.show()

            }

        }


        mainBinding.taskRV.adapter = taskViewAdapter

        callGetTaskList(taskViewAdapter)
        // Update Task End


    }

    private fun saveImageUriToSharedPreferences(uri: Uri) {
        val editor = sharedPreferences.edit()
        editor.putString("imageUri", uri.toString())
        editor.apply()
    }
    private fun retrieveImageUriFromSharedPreferences(): Uri? {
        val uriString = sharedPreferences.getString("imageUri", null)
        return uriString?.let { Uri.parse(it) }
    }

    ////capture

    private fun createImageUri(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "camera_photos_$timeStamp.png"
        val image = File(filesDir, imageFileName)
        return FileProvider.getUriForFile(this, "org.unizd.rma.babic.FileProvider", image)
    }


    private fun callGetTaskList(taskViewAdapter :TaskViewAdapter) {
        CoroutineScope(Dispatchers.Main).launch {


            taskViewModel.getTaskList().collect {

                when (it.status) {

                    Status.LOADING -> {
                        loadingDialog.show()

                    }

                    Status.SUCCESS -> {

                        it.data?.collect { taskList ->
                            loadingDialog.dismiss()
                            taskViewAdapter.addAllTask(taskList)
                        }


                    }

                    Status.ERROR -> {

                        loadingDialog.dismiss()
                        it.message?.let { it1 -> longToastShow(it1) }

                    }

                }

            }


        }


    }
}