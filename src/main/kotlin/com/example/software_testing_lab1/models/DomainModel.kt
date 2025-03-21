package com.example.software_testing_lab1.models

enum class Action {
    FLY, STAY
}

enum class WindowAppearance {
    MAJESTIC,
    ORDINARY,
    UNSIGHTLY
}

enum class Location {
    IN_FRONT_OF,
    BEHIND
}

fun toggleLocation(location: Location) = when (location) {
    Location.BEHIND -> Location.IN_FRONT_OF
    Location.IN_FRONT_OF -> Location.BEHIND
}

enum class Reaction {
    JUBILANT,
    ANGRY,
    SAD,
    SILENT
}

enum class SpeakerState {
    SPEAKING, SILENT, PAUSED
}

class Person(
    val name: String,
    var inCrowd: Boolean = false,
    var action: Action = Action.STAY,
    var reaction: Reaction = Reaction.SILENT
) {
    fun fly(): Person {
        this.action = Action.FLY
        return this
    }

    fun stay(): Person {
        this.action = Action.STAY
        return this
    }

    fun changeReaction(newReaction: Reaction): Person {
        this.reaction = newReaction
        return this
    }

    fun joinCrowd(): Person {
        this.inCrowd = true
        return this
    }

    fun leaveCrowd(): Person {
        this.inCrowd = false
        return this
    }
}

class Window(
    var appearance: WindowAppearance = WindowAppearance.ORDINARY,
    var floor: Int = 1,
    var isOpen: Boolean = false
) {
    fun changeAppearance(newAppearance: WindowAppearance): Window {
        this.appearance = newAppearance
        return this
    }

    fun changeFloor(newFloor: Int): Window {
        this.floor = newFloor
        return this
    }

    fun openWindow(): Window {
        this.isOpen = true
        return this
    }

    fun closeWindow(): Window {
        this.isOpen = false
        return this
    }

}

class Building(
    var floors: Int = 5,
    var location: Location = Location.BEHIND,
) {
    fun changeLocation(newLocation: Location): Building {
        this.location = newLocation
        return this
    }

    fun addFloor(): Building {
        this.floors += 1
        return this
    }

    fun removeFloor(): Building {
        this.floors -= 1
        return this
    }
}

class Platform(
    var location: Location = Location.IN_FRONT_OF,
) {
    fun changeLocation(newLocation: Location): Platform {
        this.location = newLocation
        return this
    }
}

class Speaker(
    var state: SpeakerState = SpeakerState.SILENT
) {

    fun startSpeaking(): Speaker {
        this.state = SpeakerState.SPEAKING
        return this
    }

    fun stopSpeaking(): Speaker {
        this.state = SpeakerState.SILENT
        return this
    }

    fun pauseSpeaking(): Speaker {
        this.state = SpeakerState.PAUSED
        return this
    }

    fun isSpeaking(): Boolean {
        return this.state == SpeakerState.SPEAKING
    }


}

class Crowd(
    var people: List<Person> = emptyList(),
) {
    fun reactToSpeaker(newReaction: Reaction): Crowd {
        this.people.forEach { it.changeReaction(newReaction) }
        return this
    }

    fun addPerson(person: Person): Crowd {
        this.people += person
        return this
    }

    fun removePerson(person: Person): Crowd {
        this.people -= person
        return this
    }
}
