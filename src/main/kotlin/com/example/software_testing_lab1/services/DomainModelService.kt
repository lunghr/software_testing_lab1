package com.example.software_testing_lab1.services

import com.example.software_testing_lab1.models.*
import org.springframework.stereotype.Service

@Service
class DomainModelService {

    fun addFloorToBuilding(building: Building): Building {
        return building.addFloor().also { println("Second: ${it.floors}") }
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


    fun startFlight(arthur: Arthur): Arthur {
        require(arthur.action == Action.STAY) { "Arthur already flying" }
        return arthur.fly()
    }

    fun stopFlight(arthur: Arthur): Arthur {
        require(arthur.action == Action.FLY) { "Arthur already staying" }
        return arthur.stay()
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


    fun changeCrowdReaction(crowd: Crowd, speaker: Speaker, crowdReaction: CrowdReaction): Crowd {
        return crowd.reactToSpeaker(speaker, crowdReaction)
    }

    fun changeSpeakerState(speaker: Speaker, crowd: Crowd): Speaker {
        return when (crowd.reaction) {
            CrowdReaction.JUBILANT -> speaker.startSpeaking()
            CrowdReaction.ANGRY -> speaker.stopSpeaking()
            CrowdReaction.SAD -> speaker.pauseSpeaking()
            CrowdReaction.SILENT -> speaker.startSpeaking()
        }
    }


    fun speakerSaidBadSpeech(speaker: Speaker, crowd: Crowd): Pair<Crowd, Speaker> {
        require(speaker.isSpeaking()) { "Speaker must be speaking" }
        val updCrowd = crowd.reactToSpeaker(speaker, CrowdReaction.ANGRY).also { println(it.reaction) }
        return Pair(updCrowd, changeSpeakerState(speaker, updCrowd))
    }

    fun speakerSaidGoodSpeech(speaker: Speaker, crowd: Crowd): Pair<Crowd, Speaker> {
        require(speaker.isSpeaking()) { "Speaker must be speaking" }
        val updCrowd = crowd.reactToSpeaker(speaker, CrowdReaction.JUBILANT)
        return Pair(updCrowd,changeSpeakerState(speaker, updCrowd))
    }

    fun addPersonToCrowd(crowd: Crowd): Crowd {
        return crowd.addPerson()
    }

    fun removePersonFromCrowd(crowd: Crowd): Crowd {
        require(crowd.isCrowdExist()) { "Crowd must have at least one person" }
        return crowd.removePerson()
    }

}