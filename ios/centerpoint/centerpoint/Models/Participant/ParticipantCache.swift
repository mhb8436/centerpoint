import UIKit

class ParticipantCache: NSObject {
    
    var participants = Array<Participant>()
    
    override init(){
        super.init()
    }
    
    init(participants: Array<Participant>) {
        super.init()
        
        participants.forEach({ (participant) in
            if !self.add(participant: participant){
                print("Fail to add participant ")
            }
        })
    }
    
    func add(participant: Participant) -> Bool {
        participants.append(participant)
        return true
    }
    
    func remove(participant: Participant) -> Bool {
        for i in 0..<participants.count {
            let existParticipant = self.participants[i]
            if existParticipant.participantId == participant.participantId {
                self.participants.remove(at: i)
                break
            }
        }
        
        return true
    }
    
    func update(oldParticipant: Participant, newParticipant: Participant) -> Bool {
        print("Participant cache oldParticipant.participantId : \(oldParticipant.participantId) , newParticipant.participantId  : \(newParticipant.participantId ), and compare : \(oldParticipant.participantId == newParticipant.participantId)")
        if oldParticipant.participantId == newParticipant.participantId {
            return self.replaceParticipant(newParticipant: newParticipant)
        }else if self.remove(participant: oldParticipant) {
            return self.add(participant: newParticipant)
        }
        return false
    }
    
    private func replaceParticipant(newParticipant: Participant) -> Bool{
        for i in 0..<participants.count {
            let participant = participants[i]
            if participant.participantId == newParticipant.participantId {
                participants[i] = newParticipant
                return true
            }
        }
        return false
    }
    
    
}
