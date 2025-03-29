import UIKit

class MeetCache: NSObject {
    
    var meets = Array<Meet>()
    
    override init(){
        super.init()
    }
    
    init(meets: Array<Meet>) {
        super.init()
        
        meets.forEach({ (meet) in
            if !self.add(meet: meet){
                print("Fail to add meet " + meet.name)
            }
        })
    }
    
    func add(meet: Meet) -> Bool {
        meets.append(meet)
        return true
    }
    
    func remove(meet: Meet) -> Bool {
        for i in 0..<meets.count {
            let existMeet = self.meets[i]
            if existMeet.meetId == meet.meetId {
                self.meets.remove(at: i)
                break
            }
        }
        
        return true
    }
    
    func update(oldMeet: Meet, newMeet: Meet) -> Bool {
        if oldMeet.meetId == newMeet.meetId {
            return self.replaceMeet(newMeet: newMeet)
        }else if self.remove(meet: oldMeet) {
            return self.add(meet: newMeet)
        }
        return false
    }
    
    private func replaceMeet(newMeet: Meet) -> Bool{
        for i in 0..<meets.count {
            let meet = meets[i]
            if meet.meetId == newMeet.meetId {
                meets[i] = newMeet
                return true
            }
        }
        return false
    }
    
    
}
