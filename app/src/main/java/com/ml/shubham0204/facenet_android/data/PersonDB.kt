package com.ml.shubham0204.facenet_android.data

import io.objectbox.annotation.Entity
import io.objectbox.kotlin.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Single

@Single
class PersonDB {
    private val personBox = ObjectBoxStore.store.boxFor(PersonRecord::class.java)

    fun addPerson(person: PersonRecord): Long {
        return personBox.put(person)
    }

    // Converts Person to PersonRecord
    fun fromPerson(person: Person): PersonRecord {
        return PersonRecord(
            personID = person.personID,
            name = person.name,
            membershipId = person.membershipId // Include membershipId
        )
    }

    // Converts PersonRecord to Person
    fun toPerson(personRecord: PersonRecord): Person {
        return Person(personID = personRecord.personID, name = personRecord.name,
            membershipId = personRecord.membershipId) // Include membershipId
    }

    fun removePerson(personID: Long) {
        personBox.removeByIds(listOf(personID))
    }

    // Returns the number of records present in the collection
    fun getCount(): Long = personBox.count()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAll(): Flow<MutableList<PersonRecord>> =
        personBox.query(PersonRecord_.personID.notNull()).build().flow().flowOn(Dispatchers.IO)
}
