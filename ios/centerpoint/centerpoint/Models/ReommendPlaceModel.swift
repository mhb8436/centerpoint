
//import UIKit
//
//class RecommendPlaceModel : NSObject {
//    private(set) var id: Int
//    private(set) var title : String
//    private(set) var score : Double
//    private(set) var detail: String
//    private(set) var location: String
//    private(set) var lat : Double
//    private(set) var lng: Double
//    private(set) var thumb_link : String
//    private(set) var thumb_img: String
//
//    init(id: Int, title: String, score: Double, detail: String, location:String, lat: Double, lng: Double, thumb_link:String, thumb_img: String){
//        self.id = id
//        self.title = title
//        self.score = score
//        self.detail = detail
//        self.location = location
//        self.lat = lat
//        self.lng = lng
//        self.thumb_img = thumb_img
//        self.thumb_link = thumb_link
//    }
//}

import Foundation

struct RecommendPlaceModel: Codable {
    let id: Int
    let title: String
    let score: Double
    let detail: String
    let location: String
    let lat: Double
    let lng: Double
    let thumb_link: String?
    let thumb_img: String?
    
    
    enum CodingKeys: String, CodingKey {
        case id
        case title
        case score
        case detail
        case location
        case lat
        case lng
        case thumb_link
        case thumb_img        
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.id   = (try? container.decode(Int.self,  forKey:.id)) ?? -1
        self.title = (try? container.decode(String.self, forKey: .title)) ?? ""
        self.score = (try? container.decode(Double.self, forKey: .score)) ?? 0.0
        self.detail = (try? container.decode(String.self, forKey: .detail)) ?? ""
        self.location = (try? container.decode(String.self, forKey: .location)) ?? ""
        self.lat = (try? container.decode(Double.self, forKey: .lat)) ?? 0.0
        self.lng = (try? container.decode(Double.self, forKey: .lng)) ?? 0.0
        self.thumb_link = (try? container.decode(String.self, forKey: .thumb_link)) ?? ""
        self.thumb_img = (try? container.decode(String.self, forKey: .thumb_img)) ?? ""

    }
    
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(self.id,   forKey: .id)
        try container.encode(self.title, forKey: .title)
        try container.encode(self.score, forKey: .score)
        try container.encode(self.detail, forKey: .detail)
        try container.encode(self.location, forKey: .location)
        try container.encode(self.lat, forKey: .lat)
        try container.encode(self.lng, forKey: .lng)
        try container.encode(self.thumb_link, forKey: .thumb_link)
        try container.encode(self.thumb_img, forKey: .thumb_img)
        
    }
    
    
}
