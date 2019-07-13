package com.test.shoppingapp.shoppinglist.repository


import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.test.networkmodule.api.ShoppingAPI
import com.test.networkmodule.response.ShopListingResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ShoppingRepository @Inject
constructor(private val shoppingAPI: ShoppingAPI) {

    private val mensShoppingList: MutableLiveData<List<ShopListingResponse>>
    private val womensShoppingList: MutableLiveData<List<ShopListingResponse>>
    private val allShoppingList: MutableLiveData<List<ShopListingResponse>>

    private val compositeDisposable: CompositeDisposable

    init {
        this.compositeDisposable = CompositeDisposable()
        this.mensShoppingList = MutableLiveData()
        this.womensShoppingList = MutableLiveData()
        this.allShoppingList = MutableLiveData()
    }

    fun getShoppingListForMen() {
        val disposable = shoppingAPI.shoppingListForMen
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ this.onMensListReceived(it) },
                { this.onApiError(it) })
        compositeDisposable.addAll(disposable)
    }

    fun getShoppingListForWomen() {
        val disposable = shoppingAPI.shoppingListForWomen
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { this.onWoensListReceived(it) },
                { this.onApiError(it) })
        compositeDisposable.addAll(disposable)
    }

    fun getShoppingListForAll() {
        val disposable = shoppingAPI.shoppingListForAll
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { this.onAllListReceived(it) },
                { this.onApiError(it) })
        compositeDisposable.addAll(disposable)
    }

    private fun onMensListReceived(shopListingResponse: List<ShopListingResponse>) {
        mensShoppingList.value = shopListingResponse
    }

    private fun onWoensListReceived(shopListingResponse: List<ShopListingResponse>) {
        womensShoppingList.value = shopListingResponse
    }

    private fun onAllListReceived(shopListingResponse: List<ShopListingResponse>) {
        allShoppingList.value = shopListingResponse
    }

    private fun onApiError(throwable: Throwable) {
        Log.e(TAG, "Error " + throwable.message, throwable)
    }

    fun getMensShoppingList(): LiveData<List<ShopListingResponse>> {
        return mensShoppingList
    }

    fun getWomensShoppingList(): LiveData<List<ShopListingResponse>> {
        return womensShoppingList
    }

    fun getAllShoppingList(): LiveData<List<ShopListingResponse>> {
        return allShoppingList
    }

    fun onCleared() {
        compositeDisposable.clear()
    }

    companion object {

        private val TAG = "ShoppingRepository"
    }

}