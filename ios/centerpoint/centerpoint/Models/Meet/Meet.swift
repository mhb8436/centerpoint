import UIKit

class Meet: NSObject {
    
    /// Invalid book identifier.
    static let MeetIdNone = 0
    
    private(set) var meetId: Int
    private(set) var name: String
    private(set) var date: Date
    private(set) var count: Int
    private(set) var prefer: String
    
    init(meetId: Int, name: String, date: Date, count: Int, prefer: String){
        self.meetId = meetId
        self.name = name
        self.date = date
        self.count = count
        self.prefer = prefer
    }
    
}
