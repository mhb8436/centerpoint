import UIKit

class ParticipantStore: NSObject {
    
    private var participantCache: ParticipantCache!
    
    private let daoFactory: DAOFactory
    
    var participants: Array<Participant> {
        get {
            return self.participantCache.participants
        }
    }
    
    
    init(daoFactory: DAOFactory) {
        self.daoFactory = daoFactory
        super.init()
        
        if let dao = self.daoFactory.participantDAO() {
            dao.create()
            self.participantCache = ParticipantCache(participants: dao.read())
        }
    }
    
    
    func add(participant: Participant) -> Bool {
        if let dao = self.daoFactory.participantDAO(), let newParticipant = dao.add(name: participant.name ?? "", type: participant.type ?? "", place:participant.place ?? "", placeId: participant.placeId ?? "", latitude:participant.latitude ?? 0.0, longitude:participant.longitude ?? 0.0, address:participant.address ?? "", meetId:participant.meetId ?? 0) {
            return self.participantCache.add(participant: newParticipant)
        }
        return false
    }
    
    func remove(participant: Participant) -> Bool {
        if let dao = self.daoFactory.participantDAO(), dao.remove(participantId: participant.participantId ?? 0) {
            return self.participantCache.remove(participant: participant)
        }
        return false
    }
    
    func update(oldParticipant: Participant, newParticipant: Participant) -> Bool {
        print("ParticipantStore cache oldParticipant.participantId : \(oldParticipant.participantId) , newParticipant.participantId  : \(newParticipant.participantId ), and compare : \(oldParticipant.participantId == newParticipant.participantId)")
        if let dao = self.daoFactory.participantDAO(), dao.update(participant: newParticipant) {            
            return self.participantCache.update(oldParticipant: oldParticipant, newParticipant: newParticipant)
        }
        return false
    }
    
}
