package com.sbd.gbd.Activities.Admin

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sbd.gbd.Adapters.ImageAdaptor
import com.sbd.gbd.R
import com.sbd.gbd.Utilitys.AppConstants
import com.sbd.gbd.Utilitys.UtilityMethods
import com.sbd.gbd.Utilitys.UtilityMethods.CompressImage
import com.sbd.gbd.Utilitys.UtilityMethods.getDistricts
import com.sbd.gbd.Utilitys.UtilityMethods.getFileName
import com.sbd.gbd.Utilitys.UtilityMethods.getTaluks
import com.sbd.gbd.databinding.ActivityAdminAdsBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class Admin_ads_dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAdsBinding
    private var CategoryName: String? = null
    private var Description: String? = null
    private var Price: String? = null
    private var Taluk: String? = null
    private var District: String? = null
    private var Pname: String? = null
    private var layoutName: String? = null
    private var bedrooms: String? = null
    private var bathrooms: String? = null
    private var floors: String? = null
    private var CategoryType: String? = null
    private var postedBy: String? = null
    private var katha: String? = null
    private var propertysize: String? = null
    private var location: String? = null
    private var number: String? = null
    private var ownerName: String? = null
    private var facing: String? = null
    private var landMark: String? = null
    private var taluk: String? = null
    private var deposite: String? = null
    private var rent: String? = null

    private var water: Boolean? = null
    private var electricty: Boolean? = null
    private var sewage: Boolean? = null
    private var corner: Boolean? = null
    private var road: Boolean? = null
    private var boarwell: Boolean? = null
    private var furnished: Boolean? = null
    private var parking: Boolean? = null
    private var gated: Boolean? = null
    private var immidateAvailable: Boolean? = null
    private var noOfAvavilableSites: String? = null

    private var approvedBy: String? = null
    private var ImageUri: Uri? = null
    private var productRandomKey: String? = null
    private var downloadImageUrl: String? = null
    private var MainimageUrl: String? = null
    private var ProductImagesRef: StorageReference? = null
    private var ProductsRef: DatabaseReference? = null
    private var loadingBar: ProgressDialog? = null
    var fileNameList: ArrayList<String> = ArrayList()
    var fileDoneList: ArrayList<String> = ArrayList()
    var locationList: ArrayList<String> = ArrayList()
    var locationMap = mutableMapOf<String, String>()
    var districtList : ArrayList<String> = ArrayList()
    private var arrayAdapter: ArrayAdapter<*>? = null
    private var districtAdapter: ArrayAdapter<*>? = null
    private lateinit var imageAdapter: ImageAdaptor
    private val selectedImages = mutableListOf<Uri>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val propertyPage = intent.getStringExtra("page")
        if (propertyPage == "2") {
            ProductImagesRef = FirebaseStorage.getInstance().reference.child("Product Images")
            ProductsRef = FirebaseDatabase.getInstance().reference.child(AppConstants.products)
        } else {
            ProductImagesRef = FirebaseStorage.getInstance().reference.child("ads")
            ProductsRef = FirebaseDatabase.getInstance().reference.child(AppConstants.ads)
        }
        binding.llLocation.adsApprovedBy.visibility = View.GONE
        loadingBar = ProgressDialog(this)
        imageAdapter = ImageAdaptor(this, selectedImages)
        binding.gridView.adapter = imageAdapter

        binding.llSelfie.setOnClickListener { OpenGallery() }
        binding.ivNavView.setOnClickListener { finish() }
        binding.llExtra.addNewAds.setOnClickListener { ValidateSitesData() }
        postedBy = "owner"
        initilize()
        getlocations()
        gettaluks("")
        
    }


    private fun initilize() {
        binding.buttonToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.owner -> { postedBy = "owner" }
                    R.id.agent -> { postedBy = "agent" }
                }
            }
        }
        binding.propertyType.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            chip?.let {
                CategoryName = it.text.toString()
                binding.llProperty.llPropDetails.visibility = View.VISIBLE

                setVisiableSite(CategoryName!!)

            }
        }
        binding.llProperty.kathaType.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            chip?.let { katha = it.text.toString() }
        }
        binding.llProperty.propertyFacing.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            chip?.let { facing = it.text.toString() }
        }
        binding.llLocation.spTaluk.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 > 0) {
                    Taluk = locationList[p2]
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        binding.llLocation.spDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 > 0) {
                    District = districtList[p2]
                    gettaluks(District!!)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        binding.llBhk.spBedrooms.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 > 0) {
                    bedrooms = resources.getStringArray(R.array.numbers).get(p2)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        binding.llBhk.spBathroomsrooms.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 > 0) {
                    bathrooms = resources.getStringArray(R.array.numbers).get(p2)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        binding.llBhk.spFloors.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 > 0) {
                    floors = resources.getStringArray(R.array.numbers).get(p2)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        binding.llExtra.waterGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.water_yes -> { water = true }
                    R.id.water_no -> { water = false }
                }
            }
        }
        binding.llExtra.electricityGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.electricity_yes -> { electricty = true }
                    R.id.electricity_no -> { electricty = false }
                }
            }
        }
        binding.llExtra.SewageGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.Sewage_yes -> { sewage = true }
                    R.id.Sewage_no -> { sewage = false }
                }
            }
        }
        binding.llExtra.cornerGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.corner_yes -> { corner = true }
                    R.id.corner_no -> { corner = false }
                }
            }
        }

        binding.llExtra.roadGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.road_yes -> { road = true }
                    R.id.road_no -> { road = false }
                }
            }
        }
        binding.llExtra.boarwellGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.boarwell_yes -> { boarwell = true }
                    R.id.boarwell_no -> { boarwell = false }
                }
            }
        }
        binding.llExtra.furnishrGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.furnish_yes -> { furnished = true }
                    R.id.furnish_no -> { furnished = false }
                }
            }
        }
        binding.llExtra.CarGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.car_yes -> { parking = true }
                    R.id.car_no -> { parking = false }
                }
            }
        }
        binding.llExtra.gatedGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.gated_yes -> { gated = true }
                    R.id.gated_no -> { gated = false }
                }
            }
        }
        binding.llExtra.immidateGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.immidate_yes -> { immidateAvailable = true }
                    R.id.immidate_no -> { immidateAvailable = false }
                }
            }
        }
    }

    private fun setVisiableSite(CategoryName: String) {
        binding.llDetailsForms.visibility = View.VISIBLE
        binding.llProperty.clPropertname.visibility = View.VISIBLE
        binding.llProperty.clPlottname.visibility = View.GONE
        binding.llBhk.formBhk.visibility = View.GONE
        binding.llProperty.clNoSites.visibility = View.GONE
        binding.llProperty.kathaType.visibility = View.VISIBLE
        binding.llProperty.tvKatha.visibility = View.VISIBLE
        binding.llProperty.clPrice.visibility = View.VISIBLE
        binding.llExtra.llRentedInfo.visibility = View.GONE
        binding.llExtra.llLandInfo.visibility = View.GONE
        binding.llExtra.llHomeInfo.visibility = View.GONE
        binding.llProperty.clRent.visibility = View.GONE
        binding.llProperty.clDeposite.visibility = View.GONE
        binding.llProperty.clNoSites.visibility = View.GONE
        binding.llProperty.clPrice.visibility = View.VISIBLE


        when(CategoryName){
            "Site" -> {
                CategoryType = "101"
                binding.llProperty.clPlottname.visibility = View.GONE
                binding.llExtra.llHomeInfo.visibility = View.VISIBLE
            }
            "Layout/Plot" -> {
                CategoryType = "102"
                binding.llProperty.clPlottname.visibility = View.VISIBLE
                binding.llProperty.clNoSites.visibility = View.VISIBLE
                binding.llProperty.clPropertname.visibility = View.GONE
                binding.llExtra.llLandInfo.visibility = View.VISIBLE
            }
            "Green Land" -> {
                CategoryType = "103"
                binding.llExtra.llLandInfo.visibility = View.VISIBLE
                binding.llProperty.clNoSites.visibility = View.GONE
                binding.llProperty.kathaType.visibility = View.GONE
                binding.llProperty.tvKatha.visibility = View.GONE
            }
            "House" -> {
                CategoryType = "104"
                binding.llProperty.clNoSites.visibility = View.GONE
                binding.llBhk.formBhk.visibility = View.VISIBLE
                binding.llExtra.llRentedInfo.visibility = View.VISIBLE
            }
            "Independent Building" -> {
                CategoryType = "105"
                binding.llProperty.clNoSites.visibility = View.GONE
                binding.llBhk.formBhk.visibility = View.VISIBLE
                binding.llExtra.llRentedInfo.visibility = View.VISIBLE
                binding.llExtra.llHomeInfo.visibility = View.VISIBLE
            }
            "House Rent" -> {
                CategoryType = "106"
                binding.llBhk.formBhk.visibility = View.VISIBLE
                binding.llExtra.llRentedInfo.visibility = View.VISIBLE
                binding.llExtra.llHomeInfo.visibility = View.VISIBLE
                binding.llProperty.clRent.visibility = View.VISIBLE
                binding.llProperty.clDeposite.visibility = View.VISIBLE
                binding.llProperty.clPrice.visibility = View.GONE
                binding.llProperty.kathaType.visibility = View.GONE
                binding.llProperty.tvKatha.visibility = View.GONE
            }
        }
    }

    private fun OpenGallery() {
        val galleryIntent = Intent()
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT)
        galleryIntent.setType("image/*")
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(galleryIntent, GalleryPick)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.data
            StoreProductInformation(data)
        }
    }

    private fun ValidateSitesData() {
         Description = binding.llExtra.adsDescription.text.toString()
         Price = binding.llProperty.adsPriceAdmin.text.toString()
        Pname = binding.llProperty.edtPropertyName.text.toString()
        propertysize = binding.llProperty.edtMeasurment.text.toString()
         location = binding.llLocation.adsLocationAdmin.text.toString()
         number = binding.llPersonal.edtContactNumber.text.toString()
         ownerName = binding.llPersonal.edtOwnerName.text.toString()
         approvedBy = binding.llLocation.adsApprovedBy.text.toString()
        landMark = binding.llLocation.adsLandmark.text.toString()
        layoutName = binding.llProperty.edtLayoutName.text.toString()

        val errorMessages = mutableListOf<String>()

        if (downloadImageUrl?.isNullOrEmpty() == true) errorMessages.add("Product image is mandatory")
        if (Description?.isNullOrEmpty() == true) errorMessages.add("Please write product description")
        if (number?.isNullOrEmpty() == true || number?.length != 10) errorMessages.add("Please enter valid contact number")
        if (CategoryName?.isNullOrEmpty() == true) errorMessages.add("Please enter category")
        if (facing?.isNullOrEmpty() == true) errorMessages.add("Please enter facing")
        if (postedBy?.isNullOrEmpty() == true) errorMessages.add("Please select whether you owner or agent")
        if (landMark?.isNullOrEmpty() == true) errorMessages.add("Please enter Landmark or City name")
        if (location?.isNullOrEmpty() == true) errorMessages.add("Please enter Location")
        if (taluk?.isNullOrEmpty() == true) errorMessages.add("Please enter Location")
        if (District?.isNullOrEmpty() == true) errorMessages.add("Please enter Location")

        when(CategoryName){
            "Site" -> {
                if (Price?.isEmpty() == true) errorMessages.add("Please write product Price")
                if (katha?.isEmpty() == true) errorMessages.add("Please enter katha type")
            }
            "Layout/Plot" -> {
                noOfAvavilableSites = binding.llProperty.edtNuSites.text.toString()
                Pname = binding.llProperty.edtLayoutName.text.toString()

                if (noOfAvavilableSites?.isNullOrEmpty() == true) errorMessages.add("Please Enter number of available sites")
                if (Price?.isNullOrEmpty() == true) errorMessages.add("Please write product Price")
                if (katha?.isNullOrEmpty() == true) errorMessages.add("Please enter katha type")
                if (layoutName?.isNullOrEmpty() == true) errorMessages.add("Please enter layout name")

            }
            "Green Land" -> {
                if (Price?.isEmpty() == true) errorMessages.add("Please write product Price")
                if (katha?.isEmpty() == true) errorMessages.add("Please enter katha type")

            }
            "House" -> {
                if (Price?.isEmpty() == true) errorMessages.add("Please write product Price")
                if (katha?.isEmpty() == true) errorMessages.add("Please enter katha type")
                if (bedrooms?.isEmpty() == true) errorMessages.add("Please enter number of Bedrooms")
                if (bathrooms?.isEmpty() == true) errorMessages.add("Please enter number of Bathrooms")
                if (floors?.isEmpty() == true) errorMessages.add("Please enter number of floors")

            }
            "Independent Building" -> {
                if (Price?.isEmpty() == true) errorMessages.add("Please write product Price")
                if (katha?.isEmpty() == true) errorMessages.add("Please enter katha type")
                if (floors?.isEmpty() == true) errorMessages.add("Please enter number of Bathrooms")
                if (bedrooms?.isEmpty() == true) errorMessages.add("Please enter number of Bedrooms")
                if (bathrooms?.isEmpty() == true) errorMessages.add("Please enter number of Bathrooms")
                if (floors?.isEmpty() == true) errorMessages.add("Please enter number of floors")

            }
            "House Rent" -> {
                deposite = binding.llLocation.adsLandmark.text.toString()
                rent = binding.llProperty.edtPriceRent.text.toString()
                if (Pname?.isNullOrEmpty() == true) errorMessages.add("Please write product name")
                if (deposite?.isNullOrEmpty() == true) errorMessages.add("Please enter Deposit Amount")
                if (rent?.isNullOrEmpty() == true) errorMessages.add("Please enter rent amount")
                if (floors?.isNullOrEmpty() == true) errorMessages.add("Please enter number of Bathrooms")
                if (bedrooms?.isNullOrEmpty() == true) errorMessages.add("Please enter number of Bedrooms")
                if (bathrooms?.isNullOrEmpty() == true) errorMessages.add("Please enter number of Bathrooms")
                if (floors?.isNullOrEmpty() == true) errorMessages.add("Please enter number of floors")
            }
        }

        if (errorMessages.isNotEmpty()) {
            //val errorMessage = errorMessages.joinToString("\n")
            Toast.makeText(this, errorMessages.get(0), Toast.LENGTH_LONG).show()
            return
        }
        saveProductInfoToDatabase()
    }

    private fun StoreProductInformation(data: Intent) {
        downloadImageUrl = ""
        if (data.data != null) {
            binding.llSelfie.visibility = View.GONE
            binding.gridView.visibility = View.VISIBLE
            val uri = data.data
            selectedImages.add(uri!!)
            uploadImageToStorage(uri)
        } else if (data.clipData != null) {
            binding.llSelfie.visibility = View.GONE
            binding.gridView.visibility = View.VISIBLE
            val clipData = data.clipData
            for (i in 0 until clipData!!.itemCount) {
                val uri = clipData.getItemAt(i).uri
                selectedImages.add(uri)
                uploadImageToStorage(uri)
            }
            imageAdapter.notifyDataSetChanged()
        }
    }

    private fun uploadImageToStorage(uri: Uri?) {
        if (uri == null) return  // Handle case where no image is selected
        val fileName = getFileName(contentResolver, uri)
        fileNameList.add(fileName!!)
        fileDoneList.add("Uploading")
         productRandomKey = UtilityMethods.getCurrentTimeDate()
        val filePath = ProductImagesRef?.child(uri.lastPathSegment + productRandomKey + ".jpg")
        val uploadTask = filePath?.putBytes(CompressImage(contentResolver,uri)) // Compress image before upload
        uploadTask?.addOnFailureListener {
            UtilityMethods.showToast(this@Admin_ads_dashboard, it.toString())
            loadingBar?.dismiss()
        }?.addOnSuccessListener {
            filePath.downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val imageUrl = task.result.toString()
                    if (downloadImageUrl?.isEmpty() == true) {
                        downloadImageUrl = imageUrl
                        MainimageUrl = imageUrl
                    } else {
                        downloadImageUrl += "---$imageUrl"
                    }
                }
            }
        }
    }

    private fun saveProductInfoToDatabase() {
        loadingBar?.setTitle("Posting New Property")
        loadingBar?.setMessage("Please wait while we are listing your property")
        loadingBar?.setCanceledOnTouchOutside(false)
        loadingBar?.show()
        val productMap = hashMapOf(
            AppConstants.pid to productRandomKey,
            AppConstants.description to Description,
            AppConstants.image2 to downloadImageUrl,
            AppConstants.image to MainimageUrl,
            AppConstants.category to CategoryName,
            AppConstants.type to CategoryType,
            AppConstants.pname to Pname,
            AppConstants.katha to katha,
            AppConstants.propertysize to propertysize,
            AppConstants.location to location,
            AppConstants.number to number,
            AppConstants.city to landMark,
            AppConstants.district to District,
            AppConstants.taluk to taluk,
            AppConstants.nearby to binding.llLocation.edtNearby.text.toString(),
            AppConstants.postedOn to UtilityMethods.getDate(),
            AppConstants.postedBy to postedBy,
            AppConstants.facing to facing,
            AppConstants.ownership to ownerName,
            AppConstants.payment to "",
            "deposit" to facing,
            "rent"  to rent,
            AppConstants.ownership to ownerName,
            AppConstants.payment to "",
            AppConstants.Status to "1",
            AppConstants.verified to "1",
            )
        if (CategoryType == "106" || CategoryType == "105" || CategoryType =="104") {
            productMap.put("bedrooms", bedrooms)
            productMap.put("bathrooms", bedrooms)
            productMap.put("floors", bedrooms)
            productMap.put("furnished", furnished.toString())
            productMap.put("parking", parking.toString())
            productMap.put("gated", gated.toString())
            productMap.put("immidate", immidateAvailable.toString())
        } else if (CategoryType == "102") {
            productMap.put("availableSites", noOfAvavilableSites)
            productMap.put("waterFacility", water.toString())
            productMap.put("electricity", electricty.toString())
            productMap.put("sewage", sewage.toString())

        } else if(CategoryType == "103") {
            productMap.put("boarwell", boarwell.toString())
            productMap.put("road", road.toString())
        } else if(CategoryType == "101") {
            productMap.put("waterFacility", water.toString())
            productMap.put("electricity", electricty.toString())
            productMap.put("sewage", sewage.toString())
        }

        ProductsRef?.child(productRandomKey!!)?.updateChildren(productMap + AppConstants.profileinfoadd(this))
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    UtilityMethods.showToast(this@Admin_ads_dashboard, "Property  added successfully..")
                    loadingBar?.dismiss()
                    finish()
                } else {
                    loadingBar?.dismiss()
                    val message = task.exception.toString()
                    UtilityMethods.showToast(this@Admin_ads_dashboard, "Error: $message")
                }
            }
    }


    fun gettaluks(district: String) {
        locationList.clear()
        locationList.add("Select Taluk")
        locationMap.forEach{
            if (district == it.value) {
                UtilityMethods.locationList.add(it.key)
            }
        }
        arrayAdapter = ArrayAdapter(
            this@Admin_ads_dashboard,
            android.R.layout.simple_spinner_item,
            locationList
        )
        arrayAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.llLocation.spTaluk.adapter = arrayAdapter
    }

    private fun getlocations() {
        val myDataRef = FirebaseDatabase.getInstance().reference.child("Locations")
        myDataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataMap = dataSnapshot.value as HashMap<*, *>?
                districtList.clear() // Clear the list before adding new data
                for (key in dataMap!!.keys) {
                    val data = dataMap[key]
                    try {
                        val userData = data as HashMap<*, *>?
                        districtList.add(userData!!["district"].toString())
                        locationMap.put(userData["taluk"].toString(), userData["district"].toString())
                    } catch (_: ClassCastException) {
                    }
                }
                districtAdapter = ArrayAdapter(
                    this@Admin_ads_dashboard,
                    android.R.layout.simple_spinner_item,
                    districtList
                )
                districtAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.llLocation.spDistrict.adapter = districtAdapter
               // binding.llLocation.spDistrict.not // Notify adapter about data change
            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to read value: ${error.message}")
            }
        })
    }

    fun getTaluks(district: String): ArrayList<String> {
        UtilityMethods.locationList.clear()
        UtilityMethods.locationList.add("Select Taluk")
        UtilityMethods.locationMap.forEach{
            if (district == it.value) {
                UtilityMethods.locationList.add(it.key)
            }
        }
        return UtilityMethods.locationList
    }

    companion object {
        private const val GalleryPick = 1
    }
}