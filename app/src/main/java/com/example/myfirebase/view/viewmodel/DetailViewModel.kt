package com.example.myfirebase.view.viewmodel

import com.example.myfirebase.modeldata.Siswa

sealed interface StatusUIDetail {
    data class Success(val satusiswa: Siswa?) : StatusUIDetail
    object Error : StatusUIDetail
    object Loading : StatusUIDetail
}