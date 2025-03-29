//import UIKit
//
//
//class TraceRouteModel : NSObject {
//
//    private(set) var no : Int
//    private(set) var lat: Double
//    private(set) var lng: Double
//    private(set) var minute: Double
//
//    init(no: Int, lat: Double, lng:Double, minute: Double){
//        self.no = no
//        self.lat = lat
//        self.lng = lng
//        self.minute = minute
//    }
//}


import Foundation

struct TraceRouteModel : Codable {
    let no : Int
    let lat: Double
    let lng: Double
    let minute: Double
    
    enum CodingKeys: String, CodingKey {
        case no
        case lat
        case lng
        case minute 
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.no   = (try? container.decode(Int.self,  forKey:.no)) ?? 0
        self.lat = (try? container.decode(Double.self, forKey: .lat)) ?? 0.0
        self.lng = (try? container.decode(Double.self, forKey: .lng)) ?? 0.0
        self.minute = (try? container.decode(Double.self, forKey: .minute)) ?? 0.0

    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(self.no,   forKey: .no)
        try container.encode(self.lat, forKey: .lat)
        try container.encode(self.lng, forKey: .lng)
        try container.encode(self.minute, forKey: .minute)
        
    }
    
}
