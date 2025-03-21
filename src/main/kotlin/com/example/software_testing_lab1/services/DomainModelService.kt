package com.example.software_testing_lab1.services

import com.example.software_testing_lab1.models.*
import org.springframework.stereotype.Service

@Service
class DomainModelService {

    fun addFloorToBuilding(building: Building): Building {
        return building.addFloor()
    }

    fun removeFloorFromBuilding(building: Building): Building {
        require(building.floors > 1) { "Building must have at least one floor" }
        return building.removeFloor()
    }

    fun changeLocationOfBuilding(location: Location, building: Building, platform: Platform): Pair<Building, Platform> {
        return location.takeIf { it != building.location }?.let {
            Pair(
                building.changeLocation(location),
                platform.changeLocation(
                    toggleLocation(platform.location)
                )
            )
        } ?: Pair(building, platform)
    }

    fun changeLocationOfPlatform(location: Location, building: Building, platform: Platform): Pair<Platform, Building> {
        return location.takeIf { it != platform.location }?.let {
            Pair(
                platform.changeLocation(location),
                building.changeLocation(
                    toggleLocation(building.location)
                )
            )
        }
            ?: Pair(platform, building)
    }


    fun startFlight(person: Person): Person {
        require(person.action == Action.STAY) { "Person already flying" }
        return person.fly()
    }

    fun stopFlight(person: Person): Person {
        require(person.action == Action.FLY) { "Person already staying" }
        return person.stay()
    }

    fun joinCrowd(person: Person, crowd: Crowd): Pair<Person, Crowd> {
        require(!person.inCrowd) { "Person already in crowd" }
        return Pair(person.joinCrowd(), crowd.addPerson(person))
    }

    fun leaveCrowd(person: Person, crowd: Crowd): Pair<Person, Crowd> {
        require(person.inCrowd) { "Person not in crowd" }
        return Pair(person.leaveCrowd(), crowd.removePerson(person))
    }

    fun changeWindowAppearance(window: Window, appearance: WindowAppearance): Window {
        return window.changeAppearance(appearance)
    }

    fun changeWindowFloor(window: Window, floor: Int, building: Building): Window {
        require(floor in 1..building.floors) { "Floor must be in building" }
        return window.changeFloor(floor)
    }


    fun openWindow(window: Window): Window {
        require(!window.isOpen) { "Window already open" }
        return window.openWindow()
    }

    fun closeWindow(window: Window): Window {
        require(window.isOpen) { "Window already closed" }
        return window.closeWindow()
    }


    fun changeCrowdReaction(crowd: Crowd, reaction: Reaction): Crowd {
        return crowd.reactToSpeaker(reaction)
    }

    fun changeSpeakerState(speaker: Speaker, crowd: Crowd): Speaker {
        val mostReaction = crowd.people
            .groupingBy { it.reaction }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
        return when (mostReaction) {
            Reaction.ANGRY -> speaker.stopSpeaking()
            Reaction.JUBILANT -> speaker.startSpeaking()
            Reaction.SAD -> speaker.pauseSpeaking()
            Reaction.SILENT -> speaker.startSpeaking()
            else -> speaker.startSpeaking()
        }
    }

    fun speakerSaidBadSpeech(speaker: Speaker, crowd: Crowd): Pair<Crowd, Speaker> {
        require(speaker.isSpeaking()) { "Speaker must be speaking" }
        val updCrowd = crowd.reactToSpeaker(Reaction.ANGRY)
        return Pair(updCrowd, changeSpeakerState(speaker, updCrowd))
    }

    fun speakerSaidGoodSpeech(speaker: Speaker, crowd: Crowd): Pair<Crowd, Speaker> {
        require(speaker.isSpeaking()) { "Speaker must be speaking" }
        val updCrowd = crowd.reactToSpeaker(Reaction.JUBILANT)
        return Pair(updCrowd, changeSpeakerState(speaker, updCrowd))
    }

    fun addPersonToCrowd(crowd: Crowd, person: Person): Pair<Crowd, Person> {
        require(person !in crowd.people) { "Person already in crowd" }
        return Pair(crowd.addPerson(person), person.joinCrowd())
    }

    fun removePersonFromCrowd(crowd: Crowd, person: Person): Pair<Crowd, Person> {
        require(person in crowd.people) { "Person not in crowd" }
        return Pair(crowd.removePerson(person), person.leaveCrowd())
    }

}