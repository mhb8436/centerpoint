//import UIKit

//class SearchPlace: NSObject {
//
//    /// Invalid book identifier.
//    static let SearchPlaceIdNone = 0
//
//    private(set) var placeId: String
//    private(set) var osmType: String
//    private(set) var osmId: String
//    private(set) var lat: Double
//    private(set) var lon: Double
//    private(set) var name: String
//    private(set) var address: String
//
//
//    init(placeId: String, osmType:String, osmId:String, lat:Double, lon:Double, name: String, address:String){
//        self.placeId = placeId
//        self.osmType = osmType
//        self.osmId = osmId
//        self.lat = lat
//        self.lon = lon
//        self.name = name
//        self.address = address
//    }
//
//}

//
//class TraceModel : NSObject {
//    static let TraceModelIdNone = 0
//
//    private(set) var id : Int
//    private(set) var name : String
//    private(set) var lat : Double
//    private(set) var lng : Double
//
//    private(set) var recommend_places: Array<RecommendPlaceModel>
//    private(set) var trace_list: Array<TraceRouteModel>
//
//    init(id: Int, name: String, lat: Double, lng: Double, recommend_places: Array<RecommendPlaceModel>, trace_list: Array<TraceRouteModel>){
//        self.id = id
//        self.name = name
//        self.lat = lat
//        self.lng = lng
//        self.recommend_places = recommend_places
//        self.trace_list = trace_list
//    }
//
//
//}

import Foundation

struct TraceModel: Codable {
    let id: Int
    let name: String
    let lat: Double
    let lng: Double
    
    let recommmend_place : [RecommendPlaceModel]
    let trace_list: [TraceRouteModel]
    
    enum CodingKeys: String, CodingKey {
        case id = "id"
        case name = "name"
        case lat = "lat"
        case lng = "lng"
        
        case recommmend_place = "recommend_place"
        case trace_list = "trace_list"
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.id   = (try? container.decode(Int.self,  forKey:.id)) ?? 0
        self.name = (try? container.decode(String.self, forKey:.name)) ?? ""
        self.lat  = (try? container.decode(Double.self, forKey:.lat)) ?? 0.0
        self.lng  = (try? container.decode(Double.self, forKey:.lng)) ?? 0.0
        self.recommmend_place = (try? container.decode([RecommendPlaceModel].self, forKey:.recommmend_place)) ?? []
        self.trace_list = (try? container.decode([TraceRouteModel].self, forKey: .trace_list)) ?? []
    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(self.id,   forKey: .id)
        try container.encode(self.name, forKey: .name)
        try container.encode(self.lat,  forKey: .lat)
        try container.encode(self.lng,  forKey: .lng)
        try container.encode(self.recommmend_place, forKey: .recommmend_place)
        try container.encode(self.trace_list, forKey: .trace_list)
        
    }
    
}
