//
//  MapViewController.swift
//  centerpoint
//
//  Created by ethan.lee781 on 15/03/2020.
//  Copyright © 2020 ethan.lee781. All rights reserved.
//

import UIKit
import GoogleMaps
import Floaty
import WebKit
import SmartWKWebView
import ALRT
import GoogleMobileAds

class MapViewController : UIViewController, GADInterstitialDelegate{
    
    var meetId: Int = -1
    var calcResult : [PointModel] = []
    var restrauntId: Int = -1
    var selectedRestraunts : [RecommendPlaceModel] = []
    var webView: WKWebView!
    var interstitial: GADInterstitial!
    var friendsMarker: [GMSMarker] = []
    var routePolyline : [GMSPolyline] = []
    
    override func loadView() {
        print("MapViewController loadView called")
        let camera = GMSCameraPosition.camera(withLatitude: 37.5125228, longitude: 126.860630796949, zoom: 11.0)
        let mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
        self.view = mapView
//        self.mapView = mapView
        
//        let marker = GMSMarker()
//        marker.position = CLLocationCoordinate2D(latitude: 36, longitude: 127)
//        marker.title = "Test"
//        marker.snippet = "Test"
//        marker.map = mapView
        interstitial = self.createAndLoadInterstitial();
        
        
        let floaty = Floaty()
        let item = FloatyItem()
        item.hasShadow = false
        item.buttonColor = UIColor.white
        item.circleShadowColor = UIColor.gray
        item.titleShadowColor = UIColor.black
        item.titleLabelPosition = .left
        item.icon = UIImage(named: "marker")
        item.title = "친구추가"
        let me = self
        item.handler = { item in
            print("item cliecked ")
            let destination = FriendViewController()
            destination.meetId = self.meetId
            me.navigationController?.pushViewController(destination, animated: true)
        }
        
        let item2 = FloatyItem()
        item2.hasShadow = false
        item2.buttonColor = UIColor.white
        item2.circleShadowColor = UIColor.gray
        item2.titleShadowColor = UIColor.black
        item2.titleLabelPosition = .left
        item2.icon = UIImage(named: "route")
        item2.title = "경로계산"
        item2.handler = { item2 in
            print("item2 cliecked ")

            if self.interstitial.isReady {
                self.interstitial.present(fromRootViewController: self)
            }else{
                print("interstitial Ad wasn't ready")
            }
            self.calculationCenterPoint()
           
        }
        floaty.addItem(item: item)
        floaty.addItem(item: item2)
        self.view.addSubview(floaty)
//        self.createAndLoadInterstitial()
        
        let longPressRecognizer = UILongPressGestureRecognizer(target: self, action: #selector(self.handleLongPress))
        self.view.addGestureRecognizer(longPressRecognizer)
        longPressRecognizer.minimumPressDuration = 0.5
        longPressRecognizer.delegate = self
        
    }
    
    @objc func handleLongPress(recognizer: UILongPressGestureRecognizer)
     {
        print("handleLongPress begin")
        if (recognizer.state == UIGestureRecognizer.State.began)
        {
            let longPressPoint = recognizer.location(in: self.view);
            let coordinate = (self.view as! GMSMapView).projection.coordinate(for: longPressPoint)
            let me = self
            print("handleLongPress is \(coordinate)")
                ALRT.create(.alert, title: "이 지점에 친구를 추가하시겠습니까?")
                    .addOK() { action, textFields in
                        print("\(action.title!) ok")
                        let destination = FriendViewController()
                        destination.meetId = self.meetId
                        destination.latitude = coordinate.latitude
                        destination.longitude = coordinate.longitude
                        me.navigationController?.pushViewController(destination, animated: true)
                    }
                    .addCancel() { action, textFields in
                        print("\(action.title!) cancel")
                    }
                    .show()
        }
    }
    
    
    func createAndLoadInterstitial() -> GADInterstitial {
      var interstitial = GADInterstitial(adUnitID: "ca-app-pub-8638613247413657/1930321008")
      interstitial.delegate = self
      interstitial.load(GADRequest())
      return interstitial
    }

    func interstitialDidDismissScreen(_ ad: GADInterstitial) {
        self.interstitial = createAndLoadInterstitial();
    }
    
    
    override func viewWillAppear(_ animated: Bool){
        super.viewWillAppear(animated)
        print("MapViewController viewWillAppear called")
        
        reloadMarker()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print("MapViewController viewDidLoad called")
        
        
//        interstitial.delegate = self
        
        (self.view as! GMSMapView).delegate = self
    }
    
    private func reloadMarker(){
        let store = self.participantStore()
        let members = store.participants;
        let participants = members.filter( { (o: Participant)  -> Bool in return (o.meetId == self.meetId) })
//        var participants:[Participant] = []
//        for o in members{
////            print("\(o.name) \(o.meetId) \(self.meetId)")
//            if o.meetId == self.meetId {
//                participants.append(o)
//            }
//        }
        print("addParticipant success is \(members) meetId is \(self.meetId)")
        for existMarker in self.friendsMarker {
            existMarker.map = nil
        }
        for participant in participants {
            print("name is \(participant.name), lat is \(participant.latitude) lng is \(participant.longitude)")
            let position = CLLocationCoordinate2D(latitude: participant.latitude!, longitude: participant.longitude!)
            let marker = GMSMarker(position: position)
            
            marker.title = "friend_" + participant.name!
            marker.icon = self.createImage(participant.name!, "bubble_blue")
            marker.map = self.view as! GMSMapView
            self.friendsMarker.append(marker)
        }
        self.moveCamera()
    }
    
    func createImage(_ name: String, _ image: String) -> UIImage {
        //count is the integer that has to be shown on the marker
        let color = UIColor.white
        // select needed color
        let string = "\(name)"
        // the string to colorize
        let attrs = [NSAttributedString.Key.foregroundColor: color, NSAttributedString.Key.font: UIFont.systemFont(ofSize: 14)]
        let attrStr = NSAttributedString(string: string, attributes: attrs)
        // add Font according to your need
        let width = 114
        let height = 72
        let fontHeight = UIFont.systemFont(ofSize: 14).pointSize
        let strWidth = string.count * fontHeight.significandWidth
        let size = (string as NSString).size(withAttributes: attrs)
        
        let image = self.resizeImage(UIImage(named: image)!, targetSize: CGSize(width: width, height: height))
        // The image on which text has to be added
        UIGraphicsBeginImageContext(image.size)
        image.draw(in: CGRect(x: CGFloat(0), y: CGFloat(0), width: CGFloat(image.size.width), height: CGFloat(image.size.height)))
//        print(" width is \(width), strWidth is \(strWidth), fontHeight is \(fontHeight.significandWidth), string.count is \(string.count), NString size is \(size)")
        let rect = CGRect(x: CGFloat( (width - size.width.significandWidth)/2 - 10 ), y: CGFloat( size.height ), width: CGFloat(image.size.width), height: CGFloat(image.size.height))
        
//        let rect = CGRect(x: CGFloat( (width - strWidth)/2 ), y: CGFloat( (height - fontHeight.significandWidth)/2 ), width: CGFloat(image.size.width), height: CGFloat(image.size.height))
//
        print(" \(rect)")
        attrStr.draw(in: rect)

        let markerImage = UIGraphicsGetImageFromCurrentImageContext()!
        UIGraphicsEndImageContext()
        return markerImage
    }
    
    func resizeImage(_ image: UIImage, targetSize: CGSize) -> UIImage {
        let size = image.size

        let widthRatio  = targetSize.width  / size.width
        let heightRatio = targetSize.height / size.height

        // Figure out what our orientation is, and use that to form the rectangle
        var newSize: CGSize
        if(widthRatio > heightRatio) {
            newSize = CGSize(width: size.width * heightRatio, height: size.height * heightRatio)
        } else {
            newSize = CGSize(width: size.width * widthRatio,  height: size.height * widthRatio)
        }

        // This is the rect that we've calculated out and this is what is actually used below
        let rect = CGRect(x: 0, y: 0, width: newSize.width, height: newSize.height)

        // Actually do the resizing to the rect using the ImageContext stuff
        UIGraphicsBeginImageContextWithOptions(newSize, false, 1.0)
        image.draw(in: rect)
        let newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()

        return newImage!
    }
    
    func participantStore() -> ParticipantStore {
        let app = UIApplication.shared.delegate as! AppDelegate
        return app.appStore.participantStore
    }
 
//    func json(from object:Any) -> String? {
//        do{
//            let jsonData = try! JSONEncoder().encode(object)
//            let jsonString = String(data: jsonData, encoding: .utf8)
//            print(jsonString)
//            return jsonString
//        }catch {print(error)}
//
//    }
   
    
    func calculationCenterPoint() -> Void {
//        let criteria: String = "";
        
        let store = self.participantStore()
        let members = store.participants;
        let participants = members.filter( { (o: Participant)  -> Bool in return (o.meetId == self.meetId) })
        
        var participantPost : [PointModel] = []
        for participant in participants {
            let item = PointModel(name: participant.name , type: participant.type!, lat: participant.latitude!, lng: participant.longitude!)
            participantPost.append(item)
        }
        let jsonData = try! JSONEncoder().encode(participantPost)
        let criteria = String(data: jsonData, encoding: .utf8)
        
        let searchUrl = URL(string : "https://api.pizzastudio.app/api-server/api/new/cp")
                        
                if let url = searchUrl {
                    var request = URLRequest(url:url)
                    let accessToken = UserDefaults.standard.string(forKey: "accessToken") as! String
                    print("2.ilterAddressInBackground acessToken \(accessToken) criteria is \(criteria)" )
                    request.setValue("Bearer " + accessToken , forHTTPHeaderField: "Authorization")
                    request.setValue("application/json", forHTTPHeaderField: "Accept")
                    request.setValue("application/json", forHTTPHeaderField: "Content-Type")
                    request.httpMethod = "POST"

                    request.httpBody = criteria?.data(using: .utf8)
                    
                    let task = URLSession.shared.dataTask(with: request, completionHandler: {(data, response, error) in
                        do {
//                            print("do data  : \(String(data:data!, encoding: .utf8))")
                            if let data = data {
                                let result = try JSONDecoder().decode([PointModel].self, from: data)
                                DispatchQueue.main.async {
                                    self.createRecommendCenterPoint(result: result)
                                }
                                
                            }else{

                            }
                        }catch{
                            
                            print("Network error : \(error)")
                            
                        }
                    })
                    task.resume()
                }
    }
    
    func createRecommendCenterPoint(result : [PointModel]) {
        print("createRecommendCenterPoint begin")
        self.calcResult = result
        self.refreshMarker()
        for p in result {
            let trList = p.tr
            if trList != nil {
                for tr in trList! {
                    let position = CLLocationCoordinate2D(latitude: tr.lat, longitude: tr.lng)
                    let marker = GMSMarker(position: position)
                    marker.title = "station_\(tr.id)"
                    marker.icon = self.createImage(tr.name, "bubble_green")
                    marker.map = self.view as! GMSMapView
                }
            }
        }
    }
    
    func refreshMarker(){
        print("refreshMarker")
        
    }
    
    func onPinRestraunt(restraunt: RecommendPlaceModel){
        print("onPinRestraunt recevie: \(restraunt)")
        let position = CLLocationCoordinate2D(latitude: restraunt.lat, longitude: restraunt.lng)
        let marker = GMSMarker(position: position)
        print("onPinRestraunt lat : \(restraunt.lat), lng : \(restraunt.lng)")
        marker.title = "restraunt_\(restraunt.id)"
        marker.icon = self.createImage(restraunt.title, "bubble_orange")
        marker.map = self.view as! GMSMapView
        
        let camera = GMSCameraPosition.camera(withLatitude: restraunt.lat ,longitude: restraunt.lng, zoom: 16)
        (self.view as! GMSMapView).camera = camera
        
    }
    
    /// Tells the delegate an ad request succeeded.
    func interstitialDidReceiveAd(_ ad: GADInterstitial) {
      print("interstitialDidReceiveAd")
    }

    /// Tells the delegate an ad request failed.
    func interstitial(_ ad: GADInterstitial, didFailToReceiveAdWithError error: GADRequestError) {
      print("interstitial:didFailToReceiveAdWithError: \(error.localizedDescription)")
    }

    /// Tells the delegate that an interstitial will be presented.
    func interstitialWillPresentScreen(_ ad: GADInterstitial) {
      print("interstitialWillPresentScreen")
    }

    /// Tells the delegate the interstitial is to be animated off the screen.
    func interstitialWillDismissScreen(_ ad: GADInterstitial) {
      print("interstitialWillDismissScreen")
    }

    /// Tells the delegate the interstitial had been animated off the screen.
//    func interstitialDidDismissScreen(_ ad: GADInterstitial) {
//      print("interstitialDidDismissScreen")
//    }

    /// Tells the delegate that a user click will open another app
    /// (such as the App Store), backgrounding the current app.
    func interstitialWillLeaveApplication(_ ad: GADInterstitial) {
      print("interstitialWillLeaveApplication")
    }
    
    func moveCamera(){
        var bounds = GMSCoordinateBounds()
        for marker in self.friendsMarker {
            bounds = bounds.includingCoordinate(marker.position)
        }
        let update = GMSCameraUpdate.fit(bounds, withPadding: 100)
        (self.view as! GMSMapView).animate(with:update)
    }
    
}

extension MapViewController: GMSMapViewDelegate {
    //class code

    @objc(mapView:didTapMarker:) func mapView(_: GMSMapView, didTap marker: GMSMarker) -> Bool {
        //do something
        print("GMSMapViewDelegate marker Clicked \(marker.title)")
        if marker.title!.contains("station_") {
            print("Station Marker clicked \(String(describing: marker.title))")
            
            ALRT.create(.alert, title: "추천경로 & 추천맛집")
                    .addAction("추천경로") { action, textFields in
                        print("\(action.title!) route")
                        for existMarker in self.friendsMarker {
                            existMarker.map = nil
                        }
                        for existLine in self.routePolyline {
                            existLine.map = nil
                        }
                        for pm in self.calcResult where pm.tr != nil {
                            let trList = pm.tr
                            if trList == nil{
                                break;
                            }
                            for case let tr in trList! {
//                                let trmList = tr.trace_list
                                var markerId = marker.title!.split{$0 == "_"}.map(String.init)
                                if Int(markerId[1]) == tr.id {
                                    let path = GMSMutablePath()
                                    var totMin = 0.0;
                                    for trm in tr.trace_list {
                                        path.add(CLLocationCoordinate2D(latitude: trm.lat, longitude: trm.lng))
                                        totMin = totMin + trm.minute
                                    }
                                    let rectangle = GMSPolyline(path: path)
                                    rectangle.strokeColor = pm.type == "metro" ? .green : .blue
                                    rectangle.strokeWidth = 5
                                    rectangle.map = (self.view as! GMSMapView)
                                    self.routePolyline.append(rectangle)
                                    
                                    print("name is \(pm.name), lat is \(pm.lat) lng is \(pm.lng)")
                                    let position = CLLocationCoordinate2D(latitude: pm.lat!, longitude: pm.lng!)
                                    let marker = GMSMarker(position: position)
                                    marker.icon = self.createImage("\(pm.name!)(\(Int(totMin))분)", "bubble_blue")
                                    marker.map = self.view as! GMSMapView
                                    self.friendsMarker.append(marker)
                                }
                            }
                            
                        }
                    }
                    .addAction("추천맛집") { action, textFields in
                        print("\(action.title!) restraunt")
                        let destination = RestrauntViewController()
                        destination.mapViewController = self
                        for p in self.calcResult {
                            let trList = p.tr
                            if trList == nil{
                                break;
                            }
                            for tr in trList! {
                                var markerId = marker.title!.split{$0 == "_"}.map(String.init)
            //                    print(markerId[1], marker.title)
                                if Int(markerId[1]) == tr.id {
                                    destination.restraunts = tr.recommmend_place
                                    self.selectedRestraunts = tr.recommmend_place
                                    print("mapView marker click : \(self.selectedRestraunts.count)")
                                }

                            }
                        }

                        self.navigationController?.pushViewController(destination, animated: true)
                    }
                    .show()
            
        }else if marker.title!.contains("restraunt_") {
            var markerId = marker.title!.split{$0 == "_"}.map(String.init)
            for p in self.calcResult {
                let trList = p.tr
                if trList == nil{
                    break;
                }
                for tr in trList! {
                    for restraunt in tr.recommmend_place {
                        if restraunt.id == Int(markerId[1]) {
//                            let vc = SmartWKWebViewController()
                            print("restraunt name is \(restraunt.title) location is \(restraunt.thumb_link)")
                            if restraunt.thumb_link != nil && !restraunt.thumb_link!.isEmpty {
                                let detailView = RestrauntWebView()
                                detailView.url = restraunt.thumb_link ?? ""
                                present(detailView, animated: true)
                                break;
                            }else{
                                ALRT.create(.alert, title: "\(restraunt.title)에 대한 블로그 정보가 없습니다.")
                                .addOK() { action, textFields in
                                    print("\(action.title!) ok")
                                }
                                .show();
                                break
                            }
                            break
                        }
                    }
                    
                }
            }
        }else if marker.title!.contains("friend_"){
            let markerId = marker.title!.split{$0 == "_"}.map(String.init)
            print("item cliecked exists \(markerId[1])")
            let store = self.participantStore()
            let members = store.participants;
            let participants = members.filter( { (o: Participant)  -> Bool in return (o.meetId == self.meetId) })
            for p in participants {
                let name = p.name ?? ""
                let mName : String! = markerId[1]
                print("name compare \(name) == \(mName ?? "") => \(name == mName)")
                if name == mName {
                    let destination = FriendViewController()
                    destination.meetId = self.meetId
                    destination.participantId = p.participantId!
                    self.navigationController?.pushViewController(destination, animated: true)
                    break
                }
            }
            
//            print("item cliecked exists \(markerId)")
//            let destination = FriendViewController()
//            destination.meetId = self.meetId
//            self.navigationController?.pushViewController(destination, animated: true)
            
        }
        return true
    }
}


extension MapViewController : UIGestureRecognizerDelegate
{
    public func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldRecognizeSimultaneouslyWith otherGestureRecognizer: UIGestureRecognizer) -> Bool
    {
        return true
    }
}
