import Foundation


//class PointModel: NSObject {
//    static let PointModelIdNone = 0
//
//    private(set) var name : String
//    private(set) var type : String
//    private(set) var lat : Double
//    private(set) var lng : Double
//
//    private(set) var tr: Array<TraceModel>
//
//    init(name: String, type: String, lat: Double, lng: Double, tr: Array<TraceModel>){
//        self.name = name
//        self.type = type
//        self.lat = lat
//        self.lng = lng
//        self.tr = tr
//    }
//}


struct PointModel:Codable {
    let name: String?
    let type: String?
    let lat: Double?
    let lng: Double?
    let tr: [TraceModel]?
    
    enum CodingKeys: String, CodingKey {
        case name = "name"
        case type = "type"
        case lat = "lat"
        case lng = "lng"
        case tr  = "tr"
    }
    
    init(name: String?, type: String?, lat:Double?, lng:Double? ){
        self.name = name
        self.type = type
        self.lat  = lat
        self.lng  = lng
        self.tr   = []
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.name = (try? container.decode(String.self, forKey:.name)) ?? ""
        self.type = (try? container.decode(String.self, forKey:.type)) ?? ""
        self.lat  = (try? container.decode(Double.self, forKey:.lat)) ?? 0.0
        self.lng  = (try? container.decode(Double.self, forKey:.lng)) ?? 0.0
        self.tr = (try? container.decode([TraceModel].self, forKey:.tr)) ?? []
    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(self.name, forKey: .name)
        try container.encode(self.type, forKey: .type)
        try container.encode(self.lat,  forKey: .lat)
        try container.encode(self.lng,  forKey: .lng)
        try container.encode(self.tr,   forKey: .tr)
    }
}
