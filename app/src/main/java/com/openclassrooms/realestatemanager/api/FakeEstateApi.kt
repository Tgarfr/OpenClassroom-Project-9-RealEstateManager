package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.model.Estate
import java.util.*

object FakeEstateApi: EstateApi {

    override fun getEstateList(): List<Estate> {
        return listOf(
            Estate(1,"Estate 1",Estate.Type.HOUSE,250000.00,100F,3, 2, 5,"Anchored bay a vast marble gallery with sweeping staircase, the entertaining floor includes a baronial living room facing Park Avenue, handsome library with original paneling, and tremendous dining room; all of which enjoy fireplaces. The state-of-the-art St. Charles designed kitchen includes a sunny breakfast room and staff quarters. Upstairs, the expansive master suite overlooks Park Avenue and includes two marble baths, two dressing rooms, and two offices. Additionally the are three large bedrooms with en-suite bath and media.",1,"Oak street","10001","New York","United States", "Apt 6/7A",1, Calendar.getInstance(),null, "Mr Bob"),
            Estate(3,"Estate 3",Estate.Type.DUPLEX,1000000.00,100F,3, 2, 5,"Anchored bay a vast marble gallery with sweeping staircase, the entertaining floor includes a baronial living room facing Park Avenue, handsome library with original paneling, and tremendous dining room; all of which enjoy fireplaces. The state-of-the-art St. Charles designed kitchen includes a sunny breakfast room and staff quarters. Upstairs, the expansive master suite overlooks Park Avenue and includes two marble baths, two dressing rooms, and two offices. Additionally the are three large bedrooms with en-suite bath and media.",1,"Oak street","10001","Montauk","United States", "Apt 6/7A",1, Calendar.getInstance(),null, "Mr Martin"),
            Estate(4,"Estate 4",Estate.Type.PENTHOUSE,75000.50,100F,3, 2, 5,"Anchored bay a vast marble gallery with sweeping staircase, the entertaining floor includes a baronial living room facing Park Avenue, handsome library with original paneling, and tremendous dining room; all of which enjoy fireplaces. The state-of-the-art St. Charles designed kitchen includes a sunny breakfast room and staff quarters. Upstairs, the expansive master suite overlooks Park Avenue and includes two marble baths, two dressing rooms, and two offices. Additionally the are three large bedrooms with en-suite bath and media.",1,"Oak street","10001","Hampton Bays","United States", "Apt 6/7A",1, Calendar.getInstance(),null, "Mr Bob"),
            Estate(5,"Estate 5",Estate.Type.FLAT,150000.30,100F,3, 2, 5,"Anchored bay a vast marble gallery with sweeping staircase, the entertaining floor includes a baronial living room facing Park Avenue, handsome library with original paneling, and tremendous dining room; all of which enjoy fireplaces. The state-of-the-art St. Charles designed kitchen includes a sunny breakfast room and staff quarters. Upstairs, the expansive master suite overlooks Park Avenue and includes two marble baths, two dressing rooms, and two offices. Additionally the are three large bedrooms with en-suite bath and media.",1,"Oak street","10001","New York","United States", "Apt 6/7A",1, Calendar.getInstance(),null, "Mr Bob")
        )
    }

}