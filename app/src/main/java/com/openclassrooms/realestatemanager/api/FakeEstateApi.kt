package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.model.Estate
import java.util.*

object FakeEstateApi: EstateApi {

    override fun getEstateList(): List<Estate> {
        return listOf(
            Estate(1,"Estate 1",Estate.Type.HOUSE,250000.00,100F,3,"The fist Estate",1,"Oak street","10001","New York",1, Calendar.getInstance(),null, "Mr Bob"),
            Estate(3,"Estate 3",Estate.Type.DUPLEX,1000000.00,100F,3,"The fist Estate",1,"Oak street","10001","New York",1, Calendar.getInstance(),null, "Mr Martin"),
            Estate(4,"Estate 4",Estate.Type.PENTHOUSE,75000.50,100F,3,"The fist Estate",1,"Oak street","10001","New York",1, Calendar.getInstance(),null, "Mr Bob"),
            Estate(5,"Estate 5",Estate.Type.FLAT,150000.30,100F,3,"The fist Estate",1,"Oak street","10001","New York",1, Calendar.getInstance(),null, "Mr Bob")
        )
    }
}