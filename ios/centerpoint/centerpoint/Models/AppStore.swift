import UIKit
import FMDB

/// Manage for the application data.
class AppStore: NSObject {
    /// Manage for the books.
    private(set) var meetStore: MeetStore!
    private(set) var participantStore: ParticipantStore!

    /// Factory of a data access objects.
    private let daoFactory = DAOFactory()

    /// Initialize the instance.
    override init() {
        super.init()
        self.meetStore = MeetStore(daoFactory: self.daoFactory)
        self.participantStore = ParticipantStore(daoFactory: self.daoFactory)
    }
}
