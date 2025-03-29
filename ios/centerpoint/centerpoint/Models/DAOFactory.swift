import UIKit
import FMDB

class DAOFactory: NSObject {
    
    private let filePath: String
    
    override init() {
        self.filePath = DAOFactory.databaseFilePath()
        super.init()
        print(self.filePath)
    }
    
    init(filePath: String) {
        self.filePath = filePath
        super.init()
    }
    
    func meetDAO() -> MeetDAO? {
        if let db = self.connect() {
            return MeetDAO(db:db)
        }
        return nil
    }
    
    func participantDAO() -> ParticipantDAO? {
        if let db = self.connect() {
            return ParticipantDAO(db:db)
        }
        return nil
    }
    
    private func connect() -> FMDatabase? {
        let db = FMDatabase(path: self.filePath)
        return (db.open()) ? db : nil
    }
    
    private static func databaseFilePath() -> String {
        let paths = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)
        let dir = paths[0] as NSString
        return dir.appendingPathComponent("app.db")
    }
}
