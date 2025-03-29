import UIKit

class MeetStore: NSObject {
    
    private var meetCache: MeetCache!
    
    private let daoFactory: DAOFactory
    
    var meets: Array<Meet> {
        get {
            return self.meetCache.meets
        }
    }
    
    
    init(daoFactory: DAOFactory) {
        self.daoFactory = daoFactory
        super.init()
        
        if let dao = self.daoFactory.meetDAO() {
            dao.create()
            self.meetCache = MeetCache(meets: dao.read())
        }
    }
    
    
    func add(meet: Meet) -> Bool {
        if let dao = self.daoFactory.meetDAO(), let newMeet = dao.add(name: meet.name, date: meet.date, count:meet.count, prefer: meet.prefer) {
            return self.meetCache.add(meet: newMeet)
        }
        return false
    }
    
    func remove(meet: Meet?) -> Bool {
        if meet != nil {
            if let dao = self.daoFactory.meetDAO(), dao.remove(meetId: meet?.meetId ?? 0) {
                return self.meetCache.remove(meet: meet!)
            }
        }
        
        return false
    }
    
    func update(oldMeet: Meet, newMeet: Meet) -> Bool {
        if let dao = self.daoFactory.meetDAO(), dao.update(meet: newMeet) {
            return self.meetCache.update(oldMeet: oldMeet, newMeet: newMeet)
        }
        return false
    }
    
}
