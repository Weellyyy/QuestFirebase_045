package com.example.myfirebase.repositori

import com.example.myfirebase.modeldata.Siswa
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface RepositorySiswa {
    suspend fun getDataSiswa(): List<Siswa>
    suspend fun postDataSiswa(siswa: Siswa)
    suspend fun getSatuSiswa(id: Long): Siswa?
    suspend fun editSatuSiswa(id: Long, siswa: Siswa)
    suspend fun hapusSatuSiswa(id: Long)
}

class FirebaseRepositorySiswa : RepositorySiswa {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("siswa")

    override suspend fun getDataSiswa(): List<Siswa> {
        return try {
            collection.get().await().documents.map { doc ->
                Siswa(
                    id = doc.id.hashCode().toLong(),
                    nama = doc.getString("nama") ?: "",
                    alamat = doc.getString("alamat") ?: "",
                    telpon = doc.getString("telpon") ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun postDataSiswa(siswa: Siswa) {
        val docRef = if (siswa.id == 0L) collection.document() else collection.document(siswa.id.toString())
        val data = hashMapOf(
            "nama" to siswa.nama,
            "alamat" to siswa.alamat,
            "telpon" to siswa.telpon
        )
        docRef.set(data).await()
    }
    override suspend fun getSatuSiswa(id: Long): Siswa? {
        return try {
            val allDocs = collection.get().await().documents
            val matchedDoc = allDocs.find { doc ->
                doc.id.hashCode().toLong() == id
            }

            matchedDoc?.let { doc ->
                Siswa(
                    id = id,
                    nama = doc.getString("nama") ?: "",
                    alamat = doc.getString("alamat") ?: "",
                    telpon = doc.getString("telpon") ?: ""
                )
            }
        } catch (e: Exception) {
            null
        }
    }
    override suspend fun editSatuSiswa(id: Long, siswa: Siswa) {
        try {
            val allDocs = collection.get().await().documents
            val matchedDoc = allDocs.find { doc ->
                doc.id.hashCode().toLong() == id
            }

            matchedDoc?.let { doc ->
                collection.document(doc.id).set(
                    mapOf(
                        "nama" to siswa.nama,
                        "alamat" to siswa.alamat,
                        "telpon" to siswa.telpon
                    )
                ).await()
            }
        } catch (e: Exception) {
        }
    }

    override suspend fun hapusSatuSiswa(id: Long) {
        try {
            val allDocs = collection.get().await().documents
            val matchedDoc = allDocs.find { doc ->
                doc.id.hashCode().toLong() == id
            }

            matchedDoc?.let { doc ->
                collection.document(doc.id).delete().await()
            }
        } catch (e: Exception) {
        }
    }

}
