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
            Pair(building.changeLocation(location),
                platform.changeLocation(
                    toggleLocation(platform.location)
                )
            )
        } ?: Pair(building, platform)
    }

    fun changeLocationOfPlatform(location: Location, building: Building, platform: Platform): Pair<Platform, Building> {
        return location.takeIf { it != platform.location }?.let {
            Pair(platform.changeLocation(location),
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


}