package com.example.software_testing_lab1

import com.example.software_testing_lab1.models.*
import com.example.software_testing_lab1.services.DomainModelService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test

class DomainModelServiceTests {
    private val domainModelService = DomainModelService()

    private lateinit var building: Building
    private lateinit var platform: Platform
    private lateinit var person1: Person
    private lateinit var person2: Person
    private lateinit var person3: Person
    private lateinit var person4: Person
    private lateinit var person5: Person
    private lateinit var arthur: Person
    private lateinit var window: Window
    private lateinit var crowd: Crowd
    private lateinit var speaker: Speaker


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

        @Test
        fun `removeFloorFromBuilding should throw exception if building has only one floor`() {
            building.floors = 0
            assertThrows<IllegalArgumentException> {
                domainModelService.removeFloorFromBuilding(building)
            }
        }
    }

    @Nested
    inner class TestArthur {
        @BeforeEach
        fun setUp() {
            arthur = Person("Arthur")
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
            val floorBigger = building.floors + 1
            assertThrows<IllegalArgumentException> {
                domainModelService.changeWindowFloor(window, floorBigger, building).also { println(it) }
            }
            val floorSmaller = 0
            assertThrows<IllegalArgumentException> {
                domainModelService.changeWindowFloor(window, floorSmaller, building).also { println(it) }
            }
        }

        @Test
        fun `changeWindowFloor should throw exception if floor is negative`() {
            val floor = -1
            assertThrows<IllegalArgumentException> {
                domainModelService.changeWindowFloor(window, floor, building).also { println(it) }
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
            person1 = Person("1")
            person2 = Person("2")
            person3 = Person("3")
            person4 = Person("4")
            person5 = Person("5")
        }

        @Test
        fun `joinCrowd should add person to crowd`() {
            val initialSize = crowd.people.size
            val result = domainModelService.joinCrowd(person1, crowd)
            assertEquals(initialSize + 1, result.second.people.size)
            assertEquals(person1, result.first)
        }

        @Test
        fun `joinCrowd should throw exception if person is already in crowd`() {
            val (person1, crowd) = domainModelService.joinCrowd(person1, crowd)
            assertThrows<IllegalArgumentException> {
                domainModelService.joinCrowd(person1, crowd)
            }
        }

        @Test
        fun `leaveCrowd should remove person from crowd`() {
            val (person1, crowd) = domainModelService.joinCrowd(person1, crowd)
            val initialSize = crowd.people.size
            val result = domainModelService.leaveCrowd(person1, crowd)
            assertEquals(initialSize - 1, result.second.people.size)
            assertEquals(person1, result.first)
        }

        @Test
        fun `leaveCrowd should throw exception if person is not in crowd`() {
            assertThrows<IllegalArgumentException> {
                domainModelService.leaveCrowd(person1, crowd)
            }
        }

        @ParameterizedTest
        @CsvSource(
            "JUBILANT",
            "ANGRY",
            "SAD",
            "SILENT"
        )
        fun `changeCrowdReaction should change reaction of all crowd members`(reaction: Reaction) {
            crowd = domainModelService.joinCrowd(person1, crowd).second
            crowd = domainModelService.joinCrowd(person2, crowd).second
            crowd = domainModelService.joinCrowd(person3, crowd).second
            crowd = domainModelService.joinCrowd(person4, crowd).second
            crowd = domainModelService.joinCrowd(person5, crowd).second
            val updatedCrowd = domainModelService.changeCrowdReaction(crowd, reaction)
            updatedCrowd.people.forEach { person ->
                assertEquals(reaction, person.reaction)
            }
        }


        @Test
        fun `speakerSaidBadSpeech should throw exception if speaker is not speaking`() {
            assertThrows<IllegalArgumentException> {
                domainModelService.speakerSaidBadSpeech(speaker, crowd)
            }
        }

        @Test
        fun `speakerSaidGoodSpeech should throw exception if speaker is not speaking`() {
            assertThrows<IllegalArgumentException> {
                domainModelService.speakerSaidGoodSpeech(speaker, crowd)
            }
        }

        @Test
        fun `speakerSaidBadSpeech should change reaction of crowd and state of speaker`() {
            crowd = domainModelService.joinCrowd(person1, crowd).second
            crowd = domainModelService.joinCrowd(person2, crowd).second
            crowd = domainModelService.joinCrowd(person3, crowd).second
            val result = domainModelService.speakerSaidBadSpeech(speaker.startSpeaking(), crowd)
            assertEquals(SpeakerState.SILENT, result.second.state)
            result.first.people.forEach { person ->
                assertEquals(Reaction.ANGRY, person.reaction)
            }
        }

        @Test
        fun `speakerSaidGoodSpeech should change reaction of crowd and state of speaker`() {
            crowd = domainModelService.joinCrowd(person1, crowd).second
            crowd = domainModelService.joinCrowd(person2, crowd).second
            crowd = domainModelService.joinCrowd(person3, crowd).second
            val result = domainModelService.speakerSaidGoodSpeech(speaker.startSpeaking(), crowd)
            assertEquals(SpeakerState.SPEAKING, result.second.state)
            result.first.people.forEach { person ->
                assertEquals(Reaction.JUBILANT, person.reaction)
            }
        }


        @Test
        fun `addPersonToCrowd should add person to crowd`() {
            val initialSize = crowd.people.size
            val result = domainModelService.addPersonToCrowd(crowd, person1)
            assertEquals(initialSize + 1, result.first.people.size)
            assertEquals(person1, result.second)
        }

        @Test
        fun `addPersonToCrowd should throw exception if person is already in crowd`() {
            crowd = domainModelService.addPersonToCrowd(crowd, person1).first
            assertThrows<IllegalArgumentException> {
                domainModelService.addPersonToCrowd(crowd, person1)
            }
        }

        @Test
        fun `removePersonFromCrowd should remove person from crowd`() {
            crowd = domainModelService.addPersonToCrowd(crowd, person1).first
            val initialSize = crowd.people.size
            val result = domainModelService.removePersonFromCrowd(crowd, person1)
            assertEquals(initialSize - 1, result.first.people.size)
            assertEquals(person1, result.second)
        }

        @Test
        fun `removePersonFromCrowd should throw exception if crowd is empty`() {
            assertThrows<IllegalArgumentException> {
                domainModelService.removePersonFromCrowd(crowd, person1)
            }
        }
    }

    @Nested
    inner class TestChangeSpeakerState{
        @BeforeEach
        fun setUp() {
            crowd = Crowd()
            speaker = Speaker()
            person1 = Person("1")
            person2 = Person("2")
            person3 = Person("3")
            person4 = Person("4")
            person5 = Person("5")
        }
        //empty
        @Test
        fun `changeSpeakerState should start speaking if crowd is empty`() {
            val updatedSpeaker = domainModelService.changeSpeakerState(speaker, crowd)
            assertEquals(SpeakerState.SPEAKING, updatedSpeaker.state)
        }

//        general reaction
        @ParameterizedTest
        @CsvSource(
            "ANGRY, SILENT",
            "JUBILANT, SPEAKING",
            "SILENT, SPEAKING",
            "SAD, PAUSED"
        )
        fun `changeSpeakingState should return correct answer for only one reaction on whole crowd`(reaction: Reaction, expectedResult: SpeakerState){
            crowd = domainModelService.joinCrowd(person1, crowd).second
            crowd = domainModelService.joinCrowd(person2, crowd).second
            crowd = domainModelService.joinCrowd(person3, crowd).second
            crowd = domainModelService.joinCrowd(person4, crowd).second
            crowd = domainModelService.joinCrowd(person5, crowd).second
            crowd = crowd.reactToSpeaker(reaction)
            val updSpeaker = domainModelService.changeSpeakerState(speaker, crowd)
            assertEquals(expectedResult, updSpeaker.state)

        }

        @Test
        fun `changeSpeakerState should identify most used reaction of crowd with every unique reaction`(){
            person1 = person1.changeReaction(Reaction.ANGRY)
            person2 = person2.changeReaction(Reaction.SAD)
            person3 = person3.changeReaction(Reaction.SILENT)
            person4 = person4.changeReaction(Reaction.JUBILANT)
            crowd = domainModelService.joinCrowd(person1, crowd).second
            crowd = domainModelService.joinCrowd(person2, crowd).second
            crowd = domainModelService.joinCrowd(person3, crowd).second
            crowd = domainModelService.joinCrowd(person4, crowd).second
            val updatedSpeaker = domainModelService.changeSpeakerState(speaker, crowd)
            assertEquals(SpeakerState.SILENT, updatedSpeaker.state)
        }

        @Test
        fun `changeSpeakerState should choose one is most used crowd reaction`() {
            person1 = person1.changeReaction(Reaction.ANGRY)
            person2 = person2.changeReaction(Reaction.ANGRY)
            person3 = person3.changeReaction(Reaction.SAD)
            person4 = person4.changeReaction(Reaction.SAD)
            person5 = person5.changeReaction(Reaction.SAD)

            crowd = domainModelService.joinCrowd(person1, crowd).second
            crowd = domainModelService.joinCrowd(person2, crowd).second
            crowd = domainModelService.joinCrowd(person3, crowd).second
            crowd = domainModelService.joinCrowd(person4, crowd).second
            crowd = domainModelService.joinCrowd(person5, crowd).second

            val updatedSpeaker = domainModelService.changeSpeakerState(speaker, crowd)
            assertEquals(SpeakerState.PAUSED, updatedSpeaker.state)
        }
    }

}