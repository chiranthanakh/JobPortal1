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
    private var CategoryName: String? = ""
    private var PropertyType: String? = ""
    private var Description: String? = ""
    private var Price: String? = ""
    private var District: String? = ""
    private var Pname: String? = ""
    private var layoutName: String? = ""
    private var bedrooms: String? = ""
    private var bathrooms: String? = ""
    private var floors: String? = ""
    private var CategoryType: String? = ""
    private var postedBy: String? = ""
    private var katha: String? = ""
    private var propertysize: String? = ""
    private var location: String? = ""
    private var rentType: String? = ""
    private var number: String? = ""
    private var ownerName: String? = ""
    private var facing: String? = ""
    private var landMark: String? = ""
    private var taluk: String? = ""
    private var deposite: String? = ""
    private var rent: String? = ""
    private var measurment: String? = ""
    private var water: Boolean? = false
    private var electricty: Boolean? = false
    private var sewage: Boolean? = false
    private var corner: Boolean? = false
    private var road: Boolean? = false
    private var fencing: Boolean? = false
    private var boarwell: Boolean? = false
    private var furnished: Boolean? = false
    private var parking: Boolean? = false
    private var gated: Boolean? = false
    private var immidateAvailable: Boolean? = false
    private var gym: Boolean? = false
    private var garden: Boolean? = false
    private var clubhouse: Boolean? = false
    private var pool: Boolean? = false
    private var nuBhk: String? = ""
    private var noOfAvavilableSites: String? = ""

    private var approvedBy: String? = ""
    private var ImageUri: Uri? = null
    private var productRandomKey: String? = ""
    private var downloadImageUrl: String? = ""
    private var MainimageUrl: String? = ""
    private var ProductImagesRef: StorageReference? = null
    private var ProductsRef: DatabaseReference? = null
    private var loadingBar: ProgressDialog? = null
    var fileNameList: ArrayList<String> = ArrayList()
    var fileDoneList: ArrayList<String> = ArrayList()
    var locationList : ArrayList<String> = ArrayList()
    var locationMap = mutableMapOf<String, String>()
    var districtList = mutableSetOf<String>()
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
//        if (propertyPage == "2") {
            ProductImagesRef = FirebaseStorage.getInstance().reference.child("Product Images")
            ProductsRef = FirebaseDatabase.getInstance().reference.child(AppConstants.products)
//        } else {
//            ProductImagesRef = FirebaseStorage.getInstance().reference.child("ads")
//            ProductsRef = FirebaseDatabase.getInstance().reference.child(AppConstants.ads)
//        }
        binding.llLocation.adsApprovedBy.visibility = View.GONE
        loadingBar = ProgressDialog(this)
        imageAdapter = ImageAdaptor(this, selectedImages)
        binding.gridView.adapter = imageAdapter

        binding.llSelfie.setOnClickListener { OpenGallery() }
        binding.ivNavView.setOnClickListener { finish() }
        binding.llExtra.addNewAds.setOnClickListener { ValidateSitesData() }

        if (propertyPage == "3") {
            binding.propertyRl.visibility = View.VISIBLE
            binding.tvPropertyRl.visibility = View.VISIBLE
            binding.tvPropertyRc.visibility = View.VISIBLE
            binding.cgPropertyTypeRl.visibility = View.VISIBLE
            binding.propertyType.visibility = View.GONE
            binding.tvPropertyType.visibility = View.GONE
        } else {
            binding.tvPropertyRc.visibility = View.GONE
            binding.cgPropertyTypeRl.visibility = View.GONE
            binding.propertyRl.visibility = View.GONE
            binding.tvPropertyRl.visibility = View.GONE
            binding.propertyType.visibility = View.VISIBLE
            binding.tvPropertyType.visibility = View.VISIBLE
        }

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
        binding.propertyRl.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            chip?.let {
                CategoryName = it.text.toString()
                binding.llProperty.llPropDetails.visibility = View.VISIBLE

                setVisiableSite(CategoryName!!)

            }
        }
        binding.cgPropertyTypeRl.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            chip?.let {
                rentType = it.text.toString()
                if (it.text.toString() == "Commercial") {
                    binding.llBhk.formBhk.visibility = View.GONE
                    binding.llExtra.llHome.visibility = View.GONE
                } else {
                    binding.llBhk.formBhk.visibility = View.VISIBLE
                    binding.llExtra.llHome.visibility = View.VISIBLE
                }
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
                    taluk = locationList[p2]
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        binding.llLocation.spDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 > 0) {
                    val list = districtList.toList()
                    District = list[p2]
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
        binding.llBhk.spBhk.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 > 0) {
                    nuBhk = resources.getStringArray(R.array.bhk).get(p2)
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
        binding.llExtra.waterGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.fencing_yes -> { fencing = true }
                    R.id.fencing_no -> { fencing = false }
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

        binding.llExtra.poolGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.pool_yes -> { pool = true }
                    R.id.pool_no -> { pool = false }
                }
            }
        }
        binding.llExtra.clubhouseGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.clubhouse_yes -> { clubhouse = true }
                    R.id.clubhouse_no -> { clubhouse = false }
                }
            }
        }
        binding.llExtra.gardenGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.garden_yes -> { garden = true }
                    R.id.garden_no -> { garden = false }
                }
            }
        }
        binding.llExtra.gynGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.gym_yes -> { gym = true }
                    R.id.gym_no -> { gym = false }
                }
            }
        }
    }


    private fun setVisiableSite(CategoryName: String) {
        binding.llDetailsForms.visibility = View.VISIBLE
        binding.llExtra.llAdditional.visibility = View.GONE
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

        binding.llExtra.llRentedInfo.visibility = View.GONE
        binding.llExtra.tvFurnished.visibility = View.VISIBLE
        binding.llExtra.furnishrGroup.visibility = View.VISIBLE
        binding.llExtra.tvCarparking.visibility = View.VISIBLE
        binding.llExtra.CarGroup.visibility = View.VISIBLE
        binding.llExtra.tvCornersight.visibility = View.VISIBLE
        binding.llExtra.cornerGroup.visibility = View.VISIBLE
        binding.llExtra.llHomeInfo.visibility = View.GONE
        binding.llExtra.tvRoad.visibility = View.VISIBLE
        binding.llExtra.roadGroup.visibility = View.VISIBLE

        when(CategoryName){
            "Site" -> {
                PropertyType = CategoryName
                CategoryType = "101"
                binding.llProperty.clPlottname.visibility = View.GONE
                binding.llExtra.llHomeInfo.visibility = View.VISIBLE
            }
            "Layout/Plot" -> {
                PropertyType = AppConstants.layoutsname
                CategoryType = "102"
                binding.llExtra.llAdditional.visibility = View.VISIBLE
                binding.llProperty.clPlottname.visibility = View.VISIBLE
                binding.llProperty.clNoSites.visibility = View.VISIBLE
                binding.llProperty.clPropertname.visibility = View.GONE
                binding.llExtra.llLandInfo.visibility = View.VISIBLE
                binding.llExtra.llRentedInfo.visibility = View.VISIBLE
                binding.llExtra.tvFurnished.visibility = View.GONE
                binding.llExtra.furnishrGroup.visibility = View.GONE
                binding.llExtra.tvCarparking.visibility = View.GONE
                binding.llExtra.CarGroup.visibility = View.GONE
                binding.llExtra.tvCornersight.visibility = View.GONE
                binding.llExtra.cornerGroup.visibility = View.GONE
                binding.llExtra.llHomeInfo.visibility = View.VISIBLE
                binding.llExtra.tvRoad.visibility = View.GONE
                binding.llExtra.roadGroup.visibility = View.GONE
            }
            "Green Land" -> {
                PropertyType = CategoryName
                CategoryType = "103"
                binding.llExtra.llLandInfo.visibility = View.VISIBLE
                binding.llProperty.clNoSites.visibility = View.GONE
                binding.llProperty.kathaType.visibility = View.GONE
                binding.llProperty.tvKatha.visibility = View.GONE
            }
            "House" -> {
                PropertyType = CategoryName
                CategoryType = "104"
                binding.llProperty.clNoSites.visibility = View.GONE
                binding.llBhk.formBhk.visibility = View.VISIBLE
                binding.llExtra.llRentedInfo.visibility = View.VISIBLE
            }
            "Independent Building" -> {
                PropertyType = CategoryName
                CategoryType = "105"
                binding.llProperty.clNoSites.visibility = View.GONE
                binding.llBhk.formBhk.visibility = View.VISIBLE
                binding.llExtra.llRentedInfo.visibility = View.VISIBLE
                binding.llExtra.llHomeInfo.visibility = View.VISIBLE
            }
            "Rent" -> {
                ProductImagesRef = FirebaseStorage.getInstance().reference.child("livingplace")
                ProductsRef = FirebaseDatabase.getInstance().reference.child("livingplaceforyou")
                PropertyType = "Rent"
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
            "Lease" -> {
                ProductImagesRef = FirebaseStorage.getInstance().reference.child("livingplace")
                ProductsRef = FirebaseDatabase.getInstance().reference.child("livingplaceforyou")
                PropertyType = "Lease"
                CategoryType = "106"
                binding.llBhk.formBhk.visibility = View.VISIBLE
                binding.llExtra.llRentedInfo.visibility = View.VISIBLE
                binding.llExtra.llHomeInfo.visibility = View.VISIBLE
                binding.llProperty.clRent.visibility = View.GONE
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
        measurment = binding.llProperty.edtMeasurment.text.toString()

        val errorMessages = mutableListOf<String>()

        if (downloadImageUrl?.isNullOrBlank() == true) errorMessages.add("Product image is mandatory")
        if (postedBy?.isNullOrEmpty() == true) errorMessages.add("Please select whether you owner or agent")
        if (measurment?.isNullOrEmpty() == true) errorMessages.add("Please add property Measurement")
        if (facing?.isNullOrEmpty() == true) errorMessages.add("Please enter facing")
        if (ownerName?.isNullOrEmpty() == true) errorMessages.add("Please enter Owner name")
        if (number?.isNullOrEmpty() == true || number?.length != 10) errorMessages.add("Please enter valid contact number")
        if (CategoryName?.isNullOrEmpty() == true) errorMessages.add("Please enter category")
        if (landMark?.isNullOrEmpty() == true) errorMessages.add("Please enter Landmark or City name")
        if (location?.isNullOrEmpty() == true) errorMessages.add("Please enter Address")
        if (taluk?.isNullOrEmpty() == true) errorMessages.add("Please select Taluk")
        if (District?.isNullOrEmpty() == true) errorMessages.add("Please select District")

        when(CategoryName){
            "Site" -> {
                if (Pname?.isNullOrEmpty() == true) errorMessages.add("Please write product name")
                if (Price?.isEmpty() == true) errorMessages.add("Please write product Price")
                if (katha?.isEmpty() == true) errorMessages.add("Please enter katha type")
            }
            "Layout/Plot" -> {
                noOfAvavilableSites = binding.llProperty.edtNuSites.text.toString()
                Pname = binding.llProperty.edtLayoutName.text.toString()
                if (Pname?.isNullOrEmpty() == true) errorMessages.add("Please write product name")
                if (noOfAvavilableSites?.isNullOrEmpty() == true) errorMessages.add("Please Enter number of available sites")
                if (Price?.isNullOrEmpty() == true) errorMessages.add("Please enter Price of property")
                if (katha?.isNullOrEmpty() == true) errorMessages.add("Please enter katha type")
                if (layoutName?.isNullOrEmpty() == true) errorMessages.add("Please enter layout name")

            }
            "Green Land" -> {
                if (Pname?.isNullOrEmpty() == true) errorMessages.add("Please write product name")
                if (Price?.isEmpty() == true) errorMessages.add("Please enter Price of property")

            }
            "House" -> {
                if (Pname?.isNullOrEmpty() == true) errorMessages.add("Please write product name")
                if (Price?.isEmpty() == true) errorMessages.add("Please enter Price of property")
                if (katha?.isEmpty() == true) errorMessages.add("Please enter katha type")
                if (bedrooms?.isEmpty() == true) errorMessages.add("Please enter number of Bedrooms")
                if (bathrooms?.isEmpty() == true) errorMessages.add("Please enter number of Bathrooms")
                if (floors?.isEmpty() == true) errorMessages.add("Please enter number of floors")

            }
            "Independent Building" -> {
                if (Pname?.isNullOrEmpty() == true) errorMessages.add("Please write product name")
                if (Price?.isEmpty() == true) errorMessages.add("Please enter Price of property")
                if (katha?.isEmpty() == true) errorMessages.add("Please enter katha type")
                if (floors?.isEmpty() == true) errorMessages.add("Please enter number of Bathrooms")
                if (bedrooms?.isEmpty() == true) errorMessages.add("Please enter number of Bedrooms")
                if (bathrooms?.isEmpty() == true) errorMessages.add("Please enter number of Bathrooms")
                if (floors?.isEmpty() == true) errorMessages.add("Please enter number of floors")

            }
            "Rent" -> {
                deposite = binding.llProperty.edtPriceDeposite.text.toString()
                rent = binding.llProperty.edtPriceRent.text.toString()
                if (Pname?.isNullOrEmpty() == true) errorMessages.add("Please write product name")
                if (deposite?.isNullOrEmpty() == true) errorMessages.add("Please enter Deposit Amount")
                if (rent?.isNullOrEmpty() == true) errorMessages.add("Please enter rent amount")
                if (rentType != "Commercial") {
                    if (floors?.isNullOrEmpty() == true) errorMessages.add("Please enter number of Bathrooms")
                    if (bedrooms?.isNullOrEmpty() == true) errorMessages.add("Please enter number of Bedrooms")
                    if (bathrooms?.isNullOrEmpty() == true) errorMessages.add("Please enter number of Bathrooms")
                    if (floors?.isNullOrEmpty() == true) errorMessages.add("Please enter number of floors")
                }
            }
            "Lease" -> {
                deposite = binding.llProperty.edtPriceDeposite.text.toString()
                rent = binding.llProperty.edtPriceRent.text.toString()
                if (Pname?.isNullOrEmpty() == true) errorMessages.add("Please write product name")
                if (deposite?.isNullOrEmpty() == true) errorMessages.add("Please enter Deposit Amount")
                if (rentType != "Commercial") {
                    if (floors?.isNullOrEmpty() == true) errorMessages.add("Please enter number of Bathrooms")
                    if (bedrooms?.isNullOrEmpty() == true) errorMessages.add("Please enter number of Bedrooms")
                    if (bathrooms?.isNullOrEmpty() == true) errorMessages.add("Please enter number of Bathrooms")
                    if (floors?.isNullOrEmpty() == true) errorMessages.add("Please enter number of floors")
                }
            }
        }
        if (Description?.isNullOrEmpty() == true) errorMessages.add("Please write product description")


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
            AppConstants.category to CategoryType,
            AppConstants.type to PropertyType,
            AppConstants.pname to Pname,
            AppConstants.katha to katha,
            AppConstants.price to Price,
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
            "deposit" to deposite,
            "rent"  to rent,
            AppConstants.ownership to ownerName,
            AppConstants.payment to "",
            AppConstants.Status to "1",
            AppConstants.verified to "1",
            )
        if ( CategoryType == "105" || CategoryType =="104") {
            productMap.put("nuBhk", nuBhk)
            productMap.put("bedrooms", bedrooms)
            productMap.put("bathrooms", bathrooms)
            productMap.put("floors", floors)
            productMap.put("furnished", furnished.toString())
            productMap.put("parking", parking.toString())
            productMap.put("gated", gated.toString())
            productMap.put("immidate", immidateAvailable.toString())
        } else if (CategoryType == "106") {
            productMap.put("nuBhk", nuBhk)
            productMap.put("buildingType", rentType)
            productMap.put("bedrooms", bedrooms)
            productMap.put("bathrooms", bathrooms)
            productMap.put("floors", floors)
            productMap.put("furnished", furnished.toString())
            productMap.put("parking", parking.toString())
            productMap.put("gated", gated.toString())
            productMap.put("immidate", immidateAvailable.toString())
        } else if (CategoryType == "102") {
            productMap.put("availableSites", noOfAvavilableSites)
            productMap.put("waterFacility", water.toString())
            productMap.put("electricity", electricty.toString())
            productMap.put("sewage", sewage.toString())
            productMap.put("gatedCommunity", gated.toString())
            productMap.put("electricity", electricty.toString())
            productMap.put("pool", pool.toString())
            productMap.put("clubhouse", clubhouse.toString())
            productMap.put("gym", gym.toString())
            productMap.put("garden", garden.toString())
            productMap.put("additionalPoints",binding.llExtra.edtAdditionalPoints.text.toString() ?: "" )

        } else if(CategoryType == "103") {
            productMap.put("boarwell", boarwell.toString())
            productMap.put("road", road.toString())
            productMap.put("fensing", fencing.toString())

        } else if(CategoryType == "101") {
            productMap.put("waterFacility", water.toString())
            productMap.put("electricity", electricty.toString())
            productMap.put("sewage", sewage.toString())
        }

        ProductsRef?.child(productRandomKey+"."+PropertyType)?.updateChildren(productMap + AppConstants.profileinfoadd(this))
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
                locationList.add(it.key)
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
                districtList.add("Select District")
                for (key in dataMap!!.keys) {
                    val data = dataMap[key]
                    try {
                        val userData = data as HashMap<*, *>?
                        districtList.add(userData?.get("district")?.toString() ?: "select District")
                        locationMap.put(userData!!["taluk"].toString(), userData["district"].toString())
                    } catch (_: ClassCastException) {
                    }
                }
                districtAdapter = ArrayAdapter(
                    this@Admin_ads_dashboard,
                    android.R.layout.simple_spinner_item,
                    districtList.toList()
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

    companion object {
        private const val GalleryPick = 1
    }
}