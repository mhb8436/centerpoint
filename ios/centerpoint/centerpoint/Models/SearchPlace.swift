import UIKit

class SearchPlace: NSObject {
    
    /// Invalid book identifier.
    static let SearchPlaceIdNone = 0
    
    private(set) var placeId: String?
    private(set) var osmType: String?
    private(set) var osmId: String?
    private(set) var lat: Double?
    private(set) var lon: Double?
    private(set) var name: String?
    private(set) var address: String?
    
    
    init(placeId: String?, osmType:String?, osmId:String?, lat:Double?, lon:Double?, name: String?, address:String?){
        self.placeId = placeId
        self.osmType = osmType
        self.osmId = osmId
        self.lat = lat
        self.lon = lon
        self.name = name
        self.address = address
    }
    
}
