import UIKit
import FMDB


class ParticipantDAO: NSObject {
    
    private static let TABLE_NAME = "participant"
    private static let  COLUMN_ID = "id";
    private static let  COLUMN_NAME = "name";
    private static let  COLUMN_TYPE = "type";
    private static let  COLUMN_PLACE = "place";
    private static let  COLUMN_PLACE_ID = "place_id";
    private static let  COLUMN_LATITUDE = "latitude";
    private static let  COLUMN_LONGITUDE = "longitude";
    private static let  COLUMN_ADDRESS = "address";
    private static let  COLUMN_MEET_ID = "meet_id";
    private static let  COLUMN_TIMESTAMP = "timestamp";

    
    private static let SQLCreate = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + COLUMN_NAME + " TEXT,"
        + COLUMN_TYPE + " TEXT,"
        + COLUMN_PLACE + " TEXT,"
        + COLUMN_PLACE_ID + " INTEGER,"
        + COLUMN_LATITUDE + " REAL,"
        + COLUMN_LONGITUDE + " REAL,"
        + COLUMN_ADDRESS + " TEXT,"
        + COLUMN_MEET_ID + " INTEGER,"
        + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
        + ")"
    
    private static let SQLSelect = ""
        + "SELECT  "
        + COLUMN_ID + ", "
        + COLUMN_NAME + ", "
        + COLUMN_TYPE + ", "
        + COLUMN_PLACE + ", "
        + COLUMN_PLACE_ID + ", "
        + COLUMN_LATITUDE + ", "
        + COLUMN_LONGITUDE + ", "
        + COLUMN_ADDRESS + ", "
        + COLUMN_MEET_ID + ", "
        + COLUMN_TIMESTAMP + " "
        + "FROM " + TABLE_NAME
    
    private static let SQLInsert = ""
        + " INSERT INTO " + TABLE_NAME + "("
        + COLUMN_NAME + ", "
        + COLUMN_TYPE + ", "
        + COLUMN_PLACE + ", "
        + COLUMN_PLACE_ID + ", "
        + COLUMN_LATITUDE + ", "
        + COLUMN_LONGITUDE + ", "
        + COLUMN_ADDRESS + ", "
        + COLUMN_MEET_ID + " "
        + " ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) "
    
    private static let SQLUpdate = ""
        + " UPDATE " + TABLE_NAME
        + " SET "
        + COLUMN_NAME + " = ? , "
        + COLUMN_TYPE + " = ? , "
        + COLUMN_PLACE + " = ? , "
        + COLUMN_PLACE_ID + " = ? , "
        + COLUMN_LATITUDE + " = ? , "
        + COLUMN_LONGITUDE + " = ? , "
        + COLUMN_ADDRESS + " = ? , "
        + COLUMN_MEET_ID + " = ?  "
        + " WHERE "
        + COLUMN_ID + " = ? "
    
    private static let SQLDelete = ""
        + " DELETE FROM  " + TABLE_NAME
        + " WHERE "
        + COLUMN_ID + " = ? "
    
    private let db: FMDatabase
    
    init(db: FMDatabase) {
        self.db = db
        super.init()
    }
    
    deinit {
        self.db.close()
    }
    
    func create() {
        self.db.executeUpdate(ParticipantDAO.SQLCreate, withArgumentsIn: [])
    }
    
    func add(name: String, type: String, place: String, placeId: String, latitude: Double, longitude: Double, address: String, meetId: Int) -> Participant? {
        var participant: Participant? = nil
        if self.db.executeUpdate(ParticipantDAO.SQLInsert, withArgumentsIn: [name, type, place, placeId, latitude, longitude, address, meetId]) {
            let participantId = db.lastInsertRowId
            participant = Participant(participantId: Int(participantId), name: name, type: type, place: place, placeId: placeId, latitude: latitude, longitude: longitude, address: address, meetId: Int(meetId))
        }
        return participant
    }
    
    func read() -> Array<Participant> {
        var participants = Array<Participant>()
        if let results = self.db.executeQuery(ParticipantDAO.SQLSelect, withArgumentsIn: []) {
            while results.next() {

                let participant = Participant(participantId: results.long(forColumnIndex: 0),
                                name: results.string(forColumnIndex: 1)!,
                                type: results.string(forColumnIndex: 2)!,
                                place: results.string(forColumnIndex: 3)!,
                                placeId: results.string(forColumnIndex: 4)!,
                                latitude: results.double(forColumnIndex: 5),
                                longitude: results.double(forColumnIndex: 6),
                                address: results.string(forColumnIndex: 7)!,
                                meetId: results.long(forColumnIndex: 8)
                )
//                print("read() \(results.long(forColumnIndex: 9)) \(results.long(forColumnIndex: 9)) \(results.long(forColumnIndex: 9))")
                participants.append(participant)
            }
        }
        return participants
    }
    
    func remove(participantId: Int) -> Bool {
        return self.db.executeUpdate(ParticipantDAO.SQLDelete, withArgumentsIn: [participantId])
    }
    
    
    func update(participant: Participant) -> Bool {
        print("Participant dao update \(participant.name!) : \(participant.type!) : \(participant.place!) : \(participant.placeId!) : \(participant.latitude!): \(participant.longitude!): \(participant.address!): \(participant.meetId!): \(participant.participantId!) ")
//            + " UPDATE " + TABLE_NAME
//            + " SET "
//            + COLUMN_NAME + " = ? , "
//            + COLUMN_TYPE + " = ? , "
//            + COLUMN_PLACE + " = ? , "
//            + COLUMN_PLACE_ID + " = ? , "
//            + COLUMN_LATITUDE + " = ? , "
//            + COLUMN_LONGITUDE + " = ? , "
//            + COLUMN_ADDRESS + " = ? , "
//            + COLUMN_MEET_ID + " = ? , "
//            + " WHERE "
//            + COLUMN_ID + " = ? "
//
        let result = db.executeUpdate(ParticipantDAO.SQLUpdate,
                                withArgumentsIn: [
                                    participant.name!,
                                    participant.type!,
                                    participant.place!,
                                    participant.placeId!,
                                    participant.latitude!,
                                    participant.longitude!,
                                    participant.address!,
                                    participant.meetId!,
                                    Int(participant.participantId!)
        ])
        print("dao update result is \(result)")
        return result
    }
}

