package com.example.campusabode

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PropertyInit{
    val propertyList: List<PropertyList> = Gson().fromJson(properties, Array<PropertyList>::class.java).asList()
    val posterTable: MutableMap<String, Int> = mutableMapOf()
    init{
        posterTable["prop1"] = R.drawable.default_img
        posterTable["prop2"] = R.drawable.default_img
        posterTable["prop3"] = R.drawable.default_img
        posterTable["prop4"] = R.drawable.default_img
        posterTable["prop5"] = R.drawable.default_img
        posterTable["prop6"] = R.drawable.default_img
        posterTable["prop7"] = R.drawable.default_img
        posterTable["prop8"] = R.drawable.default_img
        posterTable["prop9"] = R.drawable.default_img
        posterTable["prop1"] = R.drawable.default_img
        for(property in propertyList){
            property.poster_pos = posterTable[property.address]
        }
    }
}

data class PropertyList (
    @SerializedName("index") val index: Int?,
    @SerializedName("id") val id: Int?,
    @SerializedName("video") val video: Boolean?,
    @SerializedName("address") val address: String?,
    @SerializedName("poster_path") val poster_path: String?,
    @SerializedName("backdrop_path") val backdrop_path: String?,
    @SerializedName("overview")val overview: String?,
    @SerializedName("description")val description: String?,
    @SerializedName("price") var price: String?,
    @SerializedName("poster_pos") var poster_pos: Int?
): Serializable

val properties = """
                [
                {
                    "index":0,
                    "id": 19404,
                    "video": false,
                    "address": "1524 E Genesee",
                    "poster_path": "/uC6TTUhPpQCmgldGyYveKRAu8JN.jpg",
                    "backdrop_path": "/nl79FQ8xWZkhL3rDr1v2RFFR6J0.jpg",
                    "overview": "3 bedrooms, 1 bathroom",
                    "price": "$550",
                    "description": "wifi, AC, Heater, Parking"
                },
                {
                    "index":1,
                    "id": 278,
                    "video": false,
                    "address": "1320 W Genesee",
                    "poster_path": "/uC6TTUhPpQCmgldGyYveKRAu8JN.jpg",
                    "backdrop_path": "/nl79FQ8xWZkhL3rDr1v2RFFR6J0.jpg",
                    "overview": "2 bedrooms, 1 bathroom",
                    "price": "'$'650"
                },
                {
                    "index":2,
                    "id": 238,
                    "video": false,
                    "address": "109 Westcott",
                    "poster_path": "/uC6TTUhPpQCmgldGyYveKRAu8JN.jpg",
                    "backdrop_path": "/nl79FQ8xWZkhL3rDr1v2RFFR6J0.jpg",
                    "overview": "5 bedrooms, 2 bathrooms",
                    "price": "'${'$'}'450"
                },
                {
                    "index":3,
                    "id": 372058,
                    "video": false,
                    "address": "15 Lexington",
                    "poster_path": "/uC6TTUhPpQCmgldGyYveKRAu8JN.jpg",
                    "backdrop_path": "/nl79FQ8xWZkhL3rDr1v2RFFR6J0.jpg",
                    "overview": "4 bedrooms, 2 bathrooms",
                    "price": "'${'$'}'500"
                },
                {
                    "index":4,
                    "id": 424,
                    "video": false,
                    "address": "1320 Madison",
                    "poster_path": "/uC6TTUhPpQCmgldGyYveKRAu8JN.jpg",
                    "backdrop_path": "/nl79FQ8xWZkhL3rDr1v2RFFR6J0.jpg",
                    "overview": "4 bedrooms, 1 bathroom",
                    "price": "'${'$'}'450"
                },
                {
                    "index":5,
                    "id": 129,
                    "video": false,
                    "address": "1526 E Genesee",
                    "poster_path": "/uC6TTUhPpQCmgldGyYveKRAu8JN.jpg",
                    "backdrop_path": "/nl79FQ8xWZkhL3rDr1v2RFFR6J0.jpg",
                    "overview": "6 bedrooms, 2 bathrooms",
                    "price": "'${'$'}'550"
                },
                {
                    "index":6,
                    "id": 497,
                    "video": false,
                    "address": "401 Waverly Avenvue",
                    "poster_path": "/uC6TTUhPpQCmgldGyYveKRAu8JN.jpg",
                    "backdrop_path": "/nl79FQ8xWZkhL3rDr1v2RFFR6J0.jpg",
                    "overview": "2 bedrooms, 1 bathroom",
                    "price": "'${'$'}'700"
                },
                {
                    "index":7,
                    "id": 637,
                    "video": false,
                    "address": "1524 Irving Street",
                    "poster_path": "/uC6TTUhPpQCmgldGyYveKRAu8JN.jpg",
                    "backdrop_path": "/nl79FQ8xWZkhL3rDr1v2RFFR6J0.jpg",
                    "overview": "5 bedrooms, 1 bathroom",
                    "price": "'${'$'}'350"
                },
                {
                    "index":8,
                    "id": 538362,
                    "video": false,
                    "address": "201 Walnut Avenue",
                    "poster_path": "/uC6TTUhPpQCmgldGyYveKRAu8JN.jpg",
                    "backdrop_path": "/nl79FQ8xWZkhL3rDr1v2RFFR6J0.jpg",
                    "overview": "2 bedrooms, 1 bathroom",
                    "price": "'${'$'}'500"
                },
                {
                    "index":9,
                    "id": 550,
                    "video": false,
                    "address": "1091 South Beach",
                    "poster_path": "/uC6TTUhPpQCmgldGyYveKRAu8JN.jpg",
                    "backdrop_path": "/nl79FQ8xWZkhL3rDr1v2RFFR6J0.jpg",
                    "overview": "4 bedrooms, 2 bathroom",
                    "price": "'${'$'}'550"
                }
        ]
        """.trimIndent()
