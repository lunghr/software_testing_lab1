package com.example.software_testing_lab1

import com.example.software_testing_lab1.models.*
import com.example.software_testing_lab1.services.DomainModelService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
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


    @Nested
    inner class TestWindow {
        @BeforeEach
        fun setUp() {
            window = Window()
            building = Building()
        }

        @Test
        fun `changeWindowAppearance should change appearance of window`() {
            val appearance = WindowAppearance.MAJESTIC
            assertEquals(appearance, domainModelService.changeWindowAppearance(window, appearance).appearance)
        }

        @Test
        fun `changeWindowFloor should change floor of window`() {
            val floor = 2
            assertEquals(floor, domainModelService.changeWindowFloor(window, floor, building).floor)
        }

        @Test
        fun `changeWindowFloor should throw exception if floor is not in building`() {
            val floor = building.floors + 1
            assertThrows<IllegalArgumentException> {
                domainModelService.changeWindowFloor(window, floor, building)
            }
        }

        @Test
        fun `openWindow should open window`() {
            assertEquals(true, domainModelService.openWindow(window).isOpen)
        }

        @Test
        fun `closeWindow should close window`() {
            window = window.openWindow()
            assertEquals(false, domainModelService.closeWindow(window).isOpen)
        }

        @Test
        fun `openWindow should throw exception if window is already open`() {
            window = window.openWindow()
            assertThrows<IllegalArgumentException> {
                domainModelService.openWindow(window)
            }
        }

        @Test
        fun `closeWindow should throw exception if window is already closed`() {
            assertThrows<IllegalArgumentException> {
                domainModelService.closeWindow(window)
            }
        }
    }


    @Nested
    inner class TestCrowdAndSpeaker {
        @BeforeEach
        fun setUp() {
            crowd = Crowd()
            speaker = Speaker()
        }

        @ParameterizedTest
        @CsvSource(
            "SILENT, SILENT",
            "SAD, PAUSED",
            "JUBILANT, SPEAKING"
        )
        fun `changeCrowdReaction should change reaction of crowd`(reaction: CrowdReaction, speakerState: SpeakerState) {
            speaker.state = speakerState
            assertEquals(reaction, domainModelService.changeCrowdReaction(crowd, speaker, reaction).reaction)
        }

        @Test
        fun `changeSpeakerState should change state of speaker`() {
            assertEquals(SpeakerState.SPEAKING, domainModelService.changeSpeakerState(speaker, crowd).state)
        }

        @Test
        fun `speakerSaidBadSpeech should change reaction of crowd and state of speaker`() {
            val result = domainModelService.speakerSaidBadSpeech(speaker.startSpeaking(), crowd)
            assertEquals(CrowdReaction.ANGRY, result.first.reaction)
            assertEquals(SpeakerState.SILENT, result.second.state)
        }

        @Test
        fun `speakerSaidGoodSpeech should change reaction of crowd and state of speaker`() {
            val result = domainModelService.speakerSaidGoodSpeech(speaker.startSpeaking(), crowd)
            assertEquals(CrowdReaction.JUBILANT, result.first.reaction)
            assertEquals(SpeakerState.SPEAKING, result.second.state)
        }


        @Test
        fun `addPersonToCrowd should add person to crowd`() {
            assertEquals(crowd.people + 1, domainModelService.addPersonToCrowd(crowd).people)
        }

        @Test
        fun `removePersonFromCrowd should remove person from crowd`() {
            assertEquals(crowd.people - 1, domainModelService.removePersonFromCrowd(crowd).people)
        }

        @Test
        fun `removePersonFromCrowd should throw exception if crowd is empty`() {
            crowd.people = 0
            assertThrows<IllegalArgumentException> {
                domainModelService.removePersonFromCrowd(crowd)
            }
        }
    }

}