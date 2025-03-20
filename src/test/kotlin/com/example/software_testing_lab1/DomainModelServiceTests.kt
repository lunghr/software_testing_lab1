package com.example.software_testing_lab1

import com.example.software_testing_lab1.models.*
import com.example.software_testing_lab1.services.DomainModelService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class DomainModelServiceTests {
    private val domainModelService = DomainModelService()
    private lateinit var arthur: Arthur
    private lateinit var building: Building
    private lateinit var window: Window
    private lateinit var platform: Platform
    private lateinit var speaker: Speaker
    private lateinit var crowd: Crowd


    @Nested
    inner class TestBuildingAndPlatform {

        @BeforeEach
        fun setUp() {
            building = Building()
            platform = Platform()
        }

        @Test
        fun `addFloorToBuilding should add floor to building`() {
            assertEquals(building.floors + 1, domainModelService.addFloorToBuilding(building).floors)
        }

        @Test
        fun `removeFloorFromBuilding should remove floor from building`() {
            assertEquals(building.floors - 1, domainModelService.removeFloorFromBuilding(building).floors)
        }

        @Test
        fun `changeLocationOfBuilding should change location of building and platform`() {
            val location = toggleLocation(building.location)
            val result = domainModelService.changeLocationOfBuilding(location, building, platform)
            assertEquals(Pair(location, toggleLocation(location)), Pair(result.first.location, result.second.location))
        }

        @Test
        fun `changeLocationOfPlatform should change location of platform and building`() {
            val location = toggleLocation(platform.location)
            val result = domainModelService.changeLocationOfPlatform(location, building, platform)
            assertEquals(Pair(location, toggleLocation(location)), Pair(result.first.location, result.second.location))
        }

        @Test
        fun `changeLocationOfBuilding should not change location of building and platform if location is the same`() {
            val location = building.location
            val result = domainModelService.changeLocationOfBuilding(location, building, platform)
            assertEquals(Pair(location, toggleLocation(location)), Pair(result.first.location, result.second.location))
        }

        @Test
        fun `changeLocationOfPlatform should not change location of platform and building if location is the same`() {
            val location = platform.location
            val result = domainModelService.changeLocationOfPlatform(location, building, platform)
            assertEquals(Pair(location, toggleLocation(location)), Pair(result.first.location, result.second.location))
        }
    }

    @Nested
    inner class TestArthur {
        @BeforeEach
        fun setUp() {
            arthur = Arthur()
        }

        @Test
        fun `startFlight should start flight`() {
            assertEquals(Action.FLY, domainModelService.startFlight(arthur).action)
        }

        @Test
        fun `stopFlight should stop flight`() {
            arthur = arthur.fly()
            assertEquals(Action.STAY, domainModelService.stopFlight(arthur).action)
        }

        @Test
        fun `startFlight should throw exception if arthur is already flying`() {
            arthur = arthur.fly()
            assertThrows<IllegalArgumentException> {
                domainModelService.startFlight(arthur)
            }
        }

        @Test
        fun `stopFlight should throw exception if arthur is already staying`() {
            assertThrows<IllegalArgumentException> {
                domainModelService.stopFlight(arthur)
            }
        }
    }

}