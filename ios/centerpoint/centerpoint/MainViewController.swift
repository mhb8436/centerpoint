//
//  MainViewController.swift
//  centerpoint
//
//  Created by ethan.lee781 on 14/03/2020.
//  Copyright © 2020 ethan.lee781. All rights reserved.
//

import UIKit
//import PopupDialog
import ALRT
import OAuthSwift
import GoogleMobileAds

class MainViewController: UITableViewController{
    
    private var creatingMeet = false
    private static let CellIdentifier = "Cell"
    var bannerView: GADBannerView!
    var interstitial: GADInterstitial!
    
//    private let meetList = MeetAPI.
    override func viewDidLoad() {
        super.viewDidLoad();
        
        self.title = "Meet List"
        let button = UIBarButtonItem(barButtonSystemItem: UIBarButtonItem.SystemItem.add, target: self, action: #selector(didTouchCreateMeetButton(sender:)))
        self.navigationItem.leftBarButtonItem  = button
        self.navigationItem.rightBarButtonItem = self.editButtonItem
        
        self.tableView.register(MeetTableViewCell.self, forCellReuseIdentifier: MainViewController.CellIdentifier)
        bannerView = GADBannerView(adSize: kGADAdSizeBanner)
        addBannerViewToView(bannerView)
        
        bannerView.adUnitID = "ca-app-pub-8638613247413657/5086945301"
        bannerView.rootViewController = self
        bannerView.load(GADRequest())
        
//        interstitial = GADInterstitial(adUnitID: "ca-app-pub-3940256099942544/4411468910")
//        let request = GADRequest()
//        interstitial.load(request)
//        if self.interstitial.isReady {
//          self.interstitial.present(fromRootViewController: self)
//        }
        getAccessToken()
        
    }
    func addBannerViewToView(_ bannerView: GADBannerView) {
     bannerView.translatesAutoresizingMaskIntoConstraints = false
     view.addSubview(bannerView)
     view.addConstraints(
       [NSLayoutConstraint(item: bannerView,
                           attribute: .bottom,
                           relatedBy: .equal,
                           toItem: bottomLayoutGuide,
                           attribute: .top,
                           multiplier: 1,
                           constant: 0),
        NSLayoutConstraint(item: bannerView,
                           attribute: .centerX,
                           relatedBy: .equal,
                           toItem: view,
                           attribute: .centerX,
                           multiplier: 1,
                           constant: 0)
       ])
    }
    
    
    func getAccessToken() {
        print("getAccessToken begin!!!");
        
        
//        curl -d "client_id=********&client_secret=********&grant_type=client_credentials&scope=member.info.public" -X POST "http://********:********@api.pizzastudio.app/oauth2-server/oauth/token"
        let oauthUrl = URL(string : "https://********:********@api.pizzastudio.app/oauth2-server/oauth/token")!
        var request = URLRequest(url:oauthUrl)
        request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField:"Content-Type")
        request.httpMethod = "POST"
        let param = "client_id=********&client_secret=********&grant_type=client_credentials&scope=member.info.public"
        request.httpBody = param.data(using: .utf8)

//        print(request)
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard let data = data,
                let response = response as? HTTPURLResponse,
                error == nil else {
                    print("error", error ?? "Unknown Error")
                    return
            }
            guard (200 ... 299) ~= response.statusCode else {
                print("statusCode shoud be 2xx, but is \(response.statusCode)")
                return
            }

            do {
                if  let json = try JSONSerialization.jsonObject(with: data, options:.allowFragments) as? [String:Any] {
                    if let accessToken = json["access_token"] as? String {
                        print("accessToken is \(accessToken)")
                        UserDefaults.standard.set(accessToken, forKey: "accessToken")
                    }
                }
            } catch let error as NSError {
                print("Failed to load: \(error.localizedDescription)")
            }
        }
        task.resume()
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        let store = self.meetStore()
        let meets = store.meets
        return meets.count;
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: MainViewController.CellIdentifier, for: indexPath) as! MeetTableViewCell;
//        print("UITableViewCell 1 : " + String(indexPath.row))
        cell.meet = meetAtIndex(indexPath);
//        print("UITableViewCell 2 : ")
        
        return cell;
    }
    
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }

    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if (editingStyle == .delete) {
            // handle delete (by removing the data from your array and updating the tableview)
            let store = self.meetStore()
            
            let oldMeet = meetAtIndex(indexPath)
            if oldMeet != nil {
                let success = store.remove(meet: oldMeet)
                print(" delete success is " + String(success))
                self.tableView.reloadData()
            }
            
        }
    }
    
    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 100;
    }
    
    @objc func didTouchCreateMeetButton(sender: Any?) {
        self.creatingMeet = true
//        showAddDialog(animated:true)
        ALRT.create(.alert, title: "Input Meeting Title")
        .addTextField(){ textfield in
            textfield.placeholder = "Meeting Name"
            textfield.tag = 1
        }
        .addAction("Save") { _,textfields in
            for textfield in textfields ?? [] {
                if textfield.tag == 1 {
                    print(" Textfield tapped "  + textfield.text! )
//                    (meetId: Int, name: String, date: Date, count: Int, prefer: String)
                    let newMeet = Meet(meetId: Meet.MeetIdNone,
                                       name: textfield.text!,
                                       date: Date(),
                                       count: 0,
                                       prefer: ""
                                       )
                    self.addMeet(meet: newMeet)
                }
            }
            
        }
        .addCancel()
        .show()
    }
    
    func addMeet(meet: Meet) {
        let store = self.meetStore()
        var success = false
        success = store.add(meet: meet)
        if success {
            self.tableView.reloadData()
        }
    }
    
    let label:UILabel = {
        let lb = UILabel()
        lb.translatesAutoresizingMaskIntoConstraints = false
        lb.textAlignment = .center
        lb.numberOfLines = 1
//        lb.textColor = UIColor.black
        lb.font=UIFont.systemFont(ofSize: 22)
//        lb.backgroundColor = .black
        return lb
    }()
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("select " + String(indexPath.row))
        let destination = MapViewController()
        let meet = meetAtIndex(indexPath)
        if meet != nil {
            let meetId = meet?.meetId
            destination.meetId = meetId ?? 0
            navigationController?.pushViewController(destination, animated: true)
        }
    }
    
    private func meetAtIndex(_ indexPath: IndexPath) -> Meet? {
        let store = self.meetStore()
        let meets = store.meets
        var meet: Meet
        do {
            try meet = meets[indexPath.row]
        }catch {
            return nil
        }
        return meet
        
    }
    
    func meetStore() -> MeetStore {
        let app = UIApplication.shared.delegate as! AppDelegate
        return app.appStore.meetStore
    }
}
