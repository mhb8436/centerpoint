import UIKit

class Participant: NSObject {
    
    /// Invalid book identifier.
    static let ParticipantIdNone = 0
    
    private(set) var participantId: Int?
    private(set) var name: String?
    private(set) var type: String?
    private(set) var place: String?
    private(set) var placeId: String?
    private(set) var latitude: Double?
    private(set) var longitude: Double?
    private(set) var address: String?
    private(set) var meetId: Int?
    
    override init(){
        super.init()
    }
    init(participantId: Int?, name: String?, type: String?, place: String?, placeId: String?, latitude: Double?, longitude: Double?, address: String?, meetId: Int?){
        super.init()
        self.participantId = participantId
        self.name = name
        self.type = type
        self.place = place
        self.placeId = placeId
        self.latitude = latitude
        self.longitude = longitude
        self.address = address
        self.meetId = meetId
    }
    
}
