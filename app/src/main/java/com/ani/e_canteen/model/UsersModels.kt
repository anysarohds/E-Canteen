package com.ani.e_canteen.model

class UsersModels {
    var email : String? = null
    var nama : String? = null
    var uid : String? = null

     constructor() {}
    constructor(email: String?, nama: String?, uid: String?) {
        this.email = email
        this.nama = nama
        this.uid = uid
    }


}