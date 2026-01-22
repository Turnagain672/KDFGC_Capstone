package com.example.capstone2

import com.example.capstone2.data.ClubInfo
import com.example.capstone2.data.CourseData
import com.example.capstone2.data.CourseDateData
import com.example.capstone2.data.EventData
import com.example.capstone2.data.KdfgcData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class KdfgcDataTest {

    @Test
    fun courseData_hasCorrectStructure() {
        val course = CourseData(
            id = "pal",
            name = "PAL Course",
            fullName = "Canadian Firearms Safety Course",
            description = "Test description",
            price = 150,
            duration = "1 Day (8 hours)",
            prerequisites = "None",
            minAge = "12 years",
            includes = "Manual, exam, certificate",
            learnItems = listOf("Item 1", "Item 2"),
            dates = listOf(
                CourseDateData("Feb 8, 2026", "9:00 AM - 5:00 PM", 4)
            )
        )

        assertEquals("pal", course.id)
        assertEquals("PAL Course", course.name)
        assertEquals(150, course.price)
        assertEquals(1, course.dates.size)
        assertEquals(4, course.dates[0].spotsLeft)
    }

    @Test
    fun eventData_hasCorrectStructure() {
        val event = EventData(
            id = "1",
            title = "Tuesday Night Trap",
            date = "Every Tuesday",
            time = "6:00 PM",
            location = "Trap Range",
            description = "Weekly trap shooting",
            category = "weekly"
        )

        assertEquals("1", event.id)
        assertEquals("Tuesday Night Trap", event.title)
        assertEquals("weekly", event.category)
    }

    @Test
    fun clubInfo_hasCorrectStructure() {
        val clubInfo = ClubInfo(
            name = "Kelowna & District Fish and Game Club",
            address = "2319 Rifle Road, Kelowna, BC",
            phone = "(250) 762-2111",
            email = "info@kdfgc.org",
            rangeHours = "8:00 AM - Dusk",
            officeHours = "Tue & Thu 6-8 PM"
        )

        assertEquals("Kelowna & District Fish and Game Club", clubInfo.name)
        assertTrue(clubInfo.email.contains("@"))
        assertTrue(clubInfo.phone.startsWith("("))
    }

    @Test
    fun kdfgcData_combinesAllData() {
        val kdfgcData = KdfgcData(
            courses = listOf(
                CourseData("pal", "PAL", "Full Name", "Desc", 150, "1 Day", "None", "12", "Manual", listOf(), listOf())
            ),
            events = listOf(
                EventData("1", "Event", "Date", "Time", "Location", "Desc", "weekly")
            ),
            clubInfo = ClubInfo("Club", "Address", "Phone", "email@test.com", "Hours", "Office")
        )

        assertEquals(1, kdfgcData.courses.size)
        assertEquals(1, kdfgcData.events.size)
        assertNotNull(kdfgcData.clubInfo)
    }
}