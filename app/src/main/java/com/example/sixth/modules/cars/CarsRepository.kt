package com.example.sixth.modules.cars

import android.annotation.SuppressLint
import com.example.sixth.config.SixtRequestExecutor
import com.example.sixth.modules.cars.CarsViewModel.Companion.CACHED_DATA
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray

class CarsRepository(private val sixtRequestExecutor: SixtRequestExecutor) {

    @SuppressLint("CheckResult")
    fun getCars(): Single<List<CarsItem>> {
        if (CACHED_DATA.isNullOrEmpty()) {
            return singleCars().doOnSuccess {
                CACHED_DATA = it
            }
        }
        return Single.just(CACHED_DATA)
    }

    private fun singleCars(): Single<List<CarsItem>> {
        return sixtRequestExecutor.singleApi(
            endPoint = "cars",
            parser = { result ->
                parseCars(result)
            }
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun parseCars(body: String): List<CarsItem> {
        val carsList = mutableListOf<CarsItem>()
        val jsonArray = JSONArray(body)

        val jsonArrayLength = jsonArray.length()

        for (index in 0 until jsonArrayLength) {
            val jsonObject = jsonArray.optJSONObject(index)
            jsonObject?.let { carsObject ->
                carsList.add(
                    CarsItem(
                        id = carsObject.optString("id") ?: "",
                        modelIdentifier = carsObject.optString("modelIdentifier") ?: "",
                        modelName = carsObject.optString("modelName") ?: "",
                        name = carsObject.optString("name") ?: "",
                        make = carsObject.optString("make") ?: "",
                        group = carsObject.optString("group") ?: "",
                        color = carsObject.optString("color") ?: "",
                        series = carsObject.optString("series") ?: "",
                        fuelType = carsObject.optString("fuelType") ?: "",
                        fuelLevel = carsObject.optString("fuelLevel") ?: "",
                        transmission = carsObject.optString("transmission") ?: "",
                        licensePlate = carsObject.optString("licensePlate") ?: "",
                        latitude = carsObject.optDouble("latitude") ?: 0.0,
                        longitude = carsObject.optDouble("longitude") ?: 0.0,
                        innerCleanliness = carsObject.optString("innerCleanliness") ?: "",
                        carImageUrl = carsObject.optString("carImageUrl") ?: "",
                    )
                )
            }
        }
        return carsList.toList()
    }
}