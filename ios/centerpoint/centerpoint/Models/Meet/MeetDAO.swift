import UIKit
import FMDB


class MeetDAO: NSObject {
    
    private static let TABLE_NAME = "meet"
    private static let COLUMN_ID = "id"
    private static let COLUMN_NAME = "name"
    private static let COLUMN_DATE = "date"
    private static let COLUMN_COUNT = "count"
    private static let COLUMN_PREFER = "prefer"
    private static let COLUMN_TIMESTAMP = "timestamp"
    
    private static let SQLCreate = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + COLUMN_NAME + " TEXT,"
        + COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
        + COLUMN_COUNT + " INT DEFAULT 0,"
        + COLUMN_PREFER + " TEXT,"
        + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
        + ")"
    
    private static let SQLSelect = ""
        + "SELECT  "
        + COLUMN_ID + ", "
        + COLUMN_NAME + ", "
        + COLUMN_DATE + ", "
        + COLUMN_COUNT + ", "
        + COLUMN_PREFER + ", "
        + COLUMN_TIMESTAMP + " "
        + "FROM " + TABLE_NAME
    
    private static let SQLInsert = ""
        + " INSERT INTO " + TABLE_NAME + "("
        + COLUMN_NAME + ", "
        + COLUMN_DATE + ", "
        + COLUMN_COUNT + ", "
        + COLUMN_PREFER + " "
        + " ) VALUES ( ?, ?, ?, ? ) "
    
    private static let SQLUpdate = ""
        + " UPDATE " + TABLE_NAME
        + " SET "
        + COLUMN_NAME + " = ? , "
        + COLUMN_DATE + " = ? , "
        + COLUMN_COUNT + " = ? , "
        + COLUMN_PREFER + " = ? "
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
        self.db.executeUpdate(MeetDAO.SQLCreate, withArgumentsIn: [])
    }
    
    func add(name: String, date: Date, count: Int, prefer: String) -> Meet? {
        var meet: Meet? = nil
        if self.db.executeUpdate(MeetDAO.SQLInsert, withArgumentsIn: [name, date, count, prefer]) {
            let meetId = db.lastInsertRowId
            meet = Meet(meetId: Int(meetId), name: name, date: date, count: 0, prefer: prefer)
        }
        return meet
    }
    
    func read() -> Array<Meet> {
        var meets = Array<Meet>()
        if let results = self.db.executeQuery(MeetDAO.SQLSelect, withArgumentsIn: []) {
            while results.next() {
                let meet = Meet(meetId: results.long(forColumnIndex: 0),
                                name: results.string(forColumnIndex: 1)!,
                                date: results.date(forColumnIndex: 2)!,
                                count: results.long(forColumnIndex: 3),
                                prefer: results.string(forColumnIndex: 4)!)
                meets.append(meet)
            }
        }
        return meets
    }
    
    func remove(meetId: Int) -> Bool {
        return self.db.executeUpdate(MeetDAO.SQLDelete, withArgumentsIn: [meetId])
    }
    
    
    func update(meet: Meet) -> Bool {
        return db.executeUpdate(MeetDAO.SQLUpdate,
                                withArgumentsIn: [
                                    meet.name,
                                    meet.date,
                                    meet.count,
                                    meet.prefer,
                                    meet.meetId
        ])
    }
}

