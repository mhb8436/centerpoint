import UIKit
import Neon
import SearchTextField

class FriendViewController : UIViewController, UITableViewDataSource, UITableViewDelegate, UITextFieldDelegate{
    
    var meetId: Int = 0
    var latitude: Double = -1.0
    var longitude: Double = -1.0
    var participantId: Int = 0
    var currentParticipant: Participant!
    let containerView : UIView = UIView()
    let titleLabel : UILabel = UILabel()
    let nameLabel : UILabel = UILabel()
    let nameTextField: UITextField = UITextField()
    let locationLabel : UILabel = UILabel()

    let addressTextField : SearchTextField = SearchTextField()
    let addressSearchButton : UIButton = UIButton()
    
    let transportLabel : UILabel = UILabel()
    let transportSelect = UITableView()
    let transportTypes = ["지하철", "자차/버스"]
    var checked = [Bool]()
    let saveButton : UIButton = UIButton()
    let deleteButton : UIButton = UIButton()
    let emptyLabel : UILabel = UILabel()
    private static let CellIdentifier = "transportSelectCell"
    var selectedTransportType : String = "metro"
    
    @objc func OnLocationButtonClicked(){
        print("'OnLocationButtonClicked clieck'")
    }
        
    @objc func OnDeleteButtonClicked(){
        if currentParticipant != nil {
            deleteParticipant(participant: currentParticipant);
        }
        self.navigationController?.popViewController(animated: true)
    }
    
    @objc func OnSaveButtonClicked(){
//        print("OnSaveButtonClicked \(self.savedSearchPlace)")
        // init(participantId: Int, name: String, type: String, place: String, placeId: String, latitude: Double, longitude: Double, address: String, meetId: Int)
        let name = nameTextField.text!
        let placeId = self.savedSearchPlace.placeId
        let place = self.savedSearchPlace.name
        let latitude = self.savedSearchPlace.lat
        let longitude = self.savedSearchPlace.lon
        let address = self.savedSearchPlace.address
        let meetId = self.meetId
        
        
        print("OnSaveButtonClicked \(name)! : \(placeId)! : \(place)! : \(latitude)! : \(longitude)! : \(address)! : \(meetId)!")
        if self.participantId > 0 {
            let participant = Participant(participantId: currentParticipant.participantId, name: name, type: self.selectedTransportType, place: place, placeId: placeId, latitude: latitude, longitude: longitude, address: address, meetId: meetId)
            updateParticipant(participant: participant)
        }else{
            let participant = Participant(participantId: Participant.ParticipantIdNone, name: name, type: self.selectedTransportType, place: place, placeId: placeId, latitude: latitude, longitude: longitude, address: address, meetId: meetId)
            addParticipant(participant:participant)
        }
        
//        self.dismiss(animated: true, completion: nil)
        self.navigationController?.popViewController(animated: true)
    }
    
    func participantStore() -> ParticipantStore {
        let app = UIApplication.shared.delegate as! AppDelegate
        return app.appStore.participantStore
    }
    
    func meetStore() -> MeetStore {
        let app = UIApplication.shared.delegate as! AppDelegate
        return app.appStore.meetStore
    }
    
    func reloadParticipant(){
        if self.participantId > 0 {
            let store = self.participantStore()
            let members = store.participants;
            let participants = members.filter( { (o: Participant)  -> Bool in return (o.participantId == self.participantId) })
            if participants.count > 0 {
                currentParticipant = participants[0]
                nameTextField.text = currentParticipant.name
                let shortAddr = currentParticipant.address!.components(separatedBy: ",")
                if shortAddr.count > 0 {
                    self.addressTextField.text = shortAddr[0]
                }else{
                    self.addressTextField.text = currentParticipant.address
                }
                
                
                let place = SearchPlace(placeId: currentParticipant.placeId, osmType: "", osmId: currentParticipant.placeId, lat: currentParticipant.latitude, lon: currentParticipant.longitude, name: currentParticipant.address, address: currentParticipant.address)
                self.savedSearchPlace = place
                self.selectedTransportType = currentParticipant.type!
                if self.selectedTransportType == "metro" {
                    let indexPath = IndexPath(row: 0, section: 0)
                    self.transportSelect.selectRow(at: indexPath, animated: false, scrollPosition: .none)
                }else{
                    let indexPath = IndexPath(row: 1, section: 0)
                    self.transportSelect.selectRow(at: indexPath, animated: false, scrollPosition: .none)
                }
            }
        }
    }
    
    func deleteParticipant(participant: Participant){
        let store = self.participantStore()
        var success = false
        success = store.remove(participant: participant)
        print("deleteParticipant success is \(success)")
        self.updateParticipantCountOfMeetForRemove()
        
    }
    
    func addParticipant(participant: Participant) {
        let store = self.participantStore()
        var success = false
        success = store.add(participant: participant)
        print("addParticipant success is \(success)")
        self.updateParticipantCountOfMeet()
    }
    
    func updateParticipant(participant: Participant){
        let store = self.participantStore()
        var success = false
        success = store.update(oldParticipant: currentParticipant, newParticipant: participant)
        print("updateParticipant success is \(success) participant is \(participant.address)")
    }
    
    func updateParticipantCountOfMeetForRemove(){
        let store = self.meetStore()
        let meets = store.meets
        let curMeet = meets.filter {(meet) -> Bool in
            meet.meetId == self.meetId
        }
        print(" curMeet is \(curMeet)");
        
        let newMeet = Meet(meetId: self.meetId,
                           name: curMeet[0].name,
                           date: curMeet[0].date,
                           count: curMeet[0].count-1,
                           prefer: curMeet[0].prefer
        )
        let success = store.update(oldMeet: curMeet[0], newMeet: newMeet)
        print("updateParticipantCountOfMeetForRemove success is \(success)")
        
    }
    
    func updateParticipantCountOfMeet(){
        let store = self.meetStore()
        let meets = store.meets
        let curMeet = meets.filter {(meet) -> Bool in
            meet.meetId == self.meetId
        }
        print(" curMeet is \(curMeet)");
        
        let newMeet = Meet(meetId: self.meetId,
                           name: curMeet[0].name,
                           date: curMeet[0].date,
                           count: curMeet[0].count+1,
                           prefer: curMeet[0].prefer
        )
        let success = store.update(oldMeet: curMeet[0], newMeet: newMeet)
        print("updateParticipantCountOfMeet success is \(success)")
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print("FriendViewController viewDidLoad")
        titleLabel.text = "친구추가"
        titleLabel.numberOfLines = 1
        titleLabel.font = UIFont(name: "HelveticaNeue-Light", size: 24)
        titleLabel.textAlignment = .center
        
        nameLabel.text = "친구이름"
        nameLabel.numberOfLines = 1
        nameLabel.font = UIFont(name: "HelveticaNeue-Light", size: 12)

        locationLabel.text = "주소검색"
        locationLabel.numberOfLines = 1
        locationLabel.font = UIFont(name: "HelveticaNeue-Light", size: 12)

        
        addressTextField.font = UIFont(name: "HelveticaNeue-Light", size: 16)
        addressTextField.placeholder = "찾으실 주소를 입력하세요"
        addressTextField.borderStyle = UITextField.BorderStyle.line

        transportLabel.text = "교통수단"
        transportLabel.numberOfLines = 1
        transportLabel.font = UIFont(name: "HelveticaNeue-Light", size: 12)

        
        transportSelect.translatesAutoresizingMaskIntoConstraints = false
        nameTextField.placeholder = "친구 이름을 입력하세요"
        nameTextField.borderStyle = UITextField.BorderStyle.line
        
        
        saveButton.setTitle("저장", for: .normal)
        saveButton.addTarget(self, action: #selector(self.OnSaveButtonClicked), for: .touchUpInside)
        saveButton.backgroundColor = UIColor.green
        saveButton.tintColor = UIColor(red: 0.5, green: 0.5, blue: 0.5, alpha: 1.0)
        
        deleteButton.setTitle("삭제", for: .normal)
        deleteButton.addTarget(self, action: #selector(self.OnDeleteButtonClicked), for: .touchUpInside)
        deleteButton.backgroundColor = UIColor.red
        deleteButton.tintColor = UIColor(red: 0.5, green: 0.5, blue: 0.5, alpha: 1.0)

        checked = Array(repeating: false, count: transportTypes.count)
        transportSelect.translatesAutoresizingMaskIntoConstraints = false;
        transportSelect.dataSource = self
        transportSelect.delegate = self
        
        
        containerView.addSubview(titleLabel)
        containerView.addSubview(titleLabel)
        containerView.addSubview(nameLabel)
        containerView.addSubview(nameTextField)
        containerView.addSubview(locationLabel)
        containerView.addSubview(addressTextField)
        containerView.addSubview(transportLabel)
        containerView.addSubview(transportSelect)
        containerView.addSubview(saveButton)
        containerView.addSubview(deleteButton)
        view.addSubview(containerView)
        

        configureCustomSearchTextField()
        
        self.transportSelect.register(TransportSelectCell.self, forCellReuseIdentifier: FriendViewController.CellIdentifier)
        
        
        if latitude > -1.0 && longitude > -1.0 {
            self.geoCodingInBackground(latitude, longitude)
        }
        nameTextField.delegate = self
        addressTextField.delegate = self
        setupToHideKeyboardOnTapOnView()
        
        reloadParticipant()
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        if let nextField = textField.superview?.viewWithTag(textField.tag + 1) as? UITextField {
           nextField.becomeFirstResponder()
        } else {
           textField.resignFirstResponder()
        }
        return false
    }
    
    func layoutFrames(){
        print("FriendViewController layoutFrames" )
        if self.traitCollection.userInterfaceStyle == .light {
            containerView.backgroundColor = UIColor.white
        }
        
        containerView.fillSuperview()
        containerView.groupAndFill(group: .vertical, views: [titleLabel, nameLabel, nameTextField,locationLabel,addressTextField,emptyLabel,transportLabel,transportSelect,saveButton,deleteButton], padding:20)
        
        titleLabel.anchorAndFillEdge(.top, xPad: 0, yPad: 62, otherSize: 62)
        
        nameLabel.alignAndFillWidth(align: .underMatchingLeft, relativeTo: titleLabel, padding: 5, height: 12)
        nameTextField.alignAndFillWidth(align: .underCentered, relativeTo: nameLabel, padding: 5, height: 62)
        
        locationLabel.alignAndFillWidth(align: .underMatchingLeft, relativeTo: nameTextField, padding: 5, height: 12)
        addressTextField.alignAndFillWidth(align: .underCentered, relativeTo: locationLabel, padding: 5, height: 62)
        
        emptyLabel.alignAndFillWidth(align: .underCentered, relativeTo: addressTextField, padding: 5, height: 100)
        
        transportLabel.alignAndFillWidth(align: .underMatchingLeft, relativeTo: emptyLabel, padding: 5, height: 12)
        transportSelect.alignAndFillWidth(align: .underCentered, relativeTo: transportLabel, padding: 5, height: 126)

        saveButton.alignAndFillWidth(align: .underCentered, relativeTo: transportSelect, padding: 5, height: 62)
        deleteButton.alignAndFillWidth(align: .underCentered, relativeTo: saveButton, padding: 5, height: 62)
        
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        print("FriendViewController tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) ")
        return transportTypes.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt IndexPath: IndexPath) -> UITableViewCell {
        

//        print("FriendViewController tableView UITableViewCell  ")
        let cell = tableView.dequeueReusableCell(withIdentifier: FriendViewController.CellIdentifier, for: IndexPath) as! TransportSelectCell
//        print("UITableViewCell 1" + String(IndexPath.row))
        cell.name = transportTypes[IndexPath.row]
//        print("UITableViewCell 2")
        if checked[IndexPath.row] == false {
            cell.accessoryType = .none
        }else if checked[IndexPath.row] == true {
            cell.accessoryType = .checkmark
        }
        
//        cell.contentView.backgroundColor = UIColor.black
//        cell.textLabel?.textColor = UIColor.white
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("FriendViewController tableView didSelectRowAt  " + String(indexPath.row))
        tableView.deselectRow(at: indexPath, animated: true)

        
        for i in 0..<tableView.numberOfSections {
            for j in 0..<tableView.numberOfRows(inSection: i) {
                if let cell = tableView.cellForRow(at: IndexPath(row:j, section:i)) {
                    cell.accessoryType = .none
                }
            }
        }
        
        if let cell = tableView.cellForRow(at: indexPath as IndexPath) {
            if cell.accessoryType == .checkmark {
                cell.accessoryType = .none
                checked[indexPath.row] = false
            } else {
                cell.accessoryType = .checkmark
                checked[indexPath.row] = true
                if indexPath.row == 0 {
                    selectedTransportType = "metro"
                }else{
                    selectedTransportType = "car"
                }
            }
        }
        
    }
    
    func tableView(_ tableView: UITableView, didDeselectRowAt indexPath: IndexPath){
        print("FriendViewController tableView didDeselectRowAt  " + String(indexPath.row))
        if let cell = tableView.cellForRow(at: indexPath) {
            cell.accessoryType = .none
        }
        
    }
    
    
    override func viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
        print("FriendViewController viewWillLayoutSubviews" )
        layoutFrames()
    }
    
    fileprivate func configureCustomSearchTextField() {
        addressTextField.theme = SearchTextFieldTheme.lightTheme()
        
        let header = UILabel(frame: CGRect(x:0, y:0, width: addressTextField.frame.width, height: 30))
//        header.backgroundColor = UIColor.lightGray.withAlphaComponent(0.3)
        header.textAlignment = .center
        header.font = UIFont.systemFont(ofSize: 14)
        header.text = "장소를 선택하세요"
        addressTextField.resultsListHeader = header
        
        addressTextField.theme.font = UIFont.systemFont(ofSize: 12)
//        addressTextField.theme.bgColor = UIColor.lightGray.withAlphaComponent(0.2)
//        addressTextField.theme.borderColor = UIColor.lightGray.withAlphaComponent(0.5)
//        addressTextField.theme.separatorColor = UIColor.lightGray.withAlphaComponent(0.5)
        addressTextField.theme.cellHeight = 50
//        addressTextField.theme.placeholderColor = UIColor.lightGray
        
        addressTextField.maxNumberOfResults = 5
        addressTextField.maxResultsListHeight = 200
        addressTextField.comparisonOptions = [.caseInsensitive]
        addressTextField.forceRightToLeft = false
        addressTextField.highlightAttributes = [NSAttributedString.Key.backgroundColor: UIColor.yellow, NSAttributedString.Key.font: UIFont.boldSystemFont(ofSize: 12)]
        addressTextField.itemSelectionHandler = { filteredResult, itemPosition in
            let item = filteredResult[itemPosition]
            print("Item at position \(itemPosition): \(item.title)")
            self.savedSearchPlace = self.searchPlaceList[itemPosition]
            let shortAddr = item.title.components(separatedBy: ",")
            if shortAddr.count > 0 {
                self.addressTextField.text = shortAddr[0]
            }else{
                self.addressTextField.text = item.title
            }
        }
        
        addressTextField.userStoppedTypingHandler = {
            if let criteria = self.addressTextField.text {
                if criteria.count > 2 {
                    self.addressTextField.showLoadingIndicator()
                    self.filterAddressInBackground(criteria) { results in
                        self.addressTextField.filterItems(results)
                        self.addressTextField.stopLoadingIndicator()
                    }
                }
            }
        }
    }
    var searchPlaceList = [SearchPlace]()
    var savedSearchPlace : SearchPlace!
    
    fileprivate func filterAddressInBackground(_ criteria: String, callback: @escaping ((_ results: [SearchTextFieldItem]) -> Void)) {
            let searchUrl = URL(string : "https://api.pizzastudio.app/api-server/api/place/search?format=json&addressdetails=1&q=" + (criteria.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed) ?? ""))
            print("1.ilterAddressInBackground called \(searchUrl) ... \(criteria)" )
            
            if let url = searchUrl {
                var request = URLRequest(url:url)
                let accessToken = UserDefaults.standard.string(forKey: "accessToken")
                print("2.ilterAddressInBackground acessToken \(accessToken!)" )
                request.setValue("Bearer \(accessToken!)"  , forHTTPHeaderField: "Authorization")
                let task = URLSession.shared.dataTask(with: request, completionHandler: {(data, response, error) in
                    do {
                        if let data = data {
                            print("3.ilterAddressInBackground data \(data)" )
//                            let jsonData = try JSONSerialization.jsonObject(with: data, options:.allowFragments)
                            let jsonData = try? JSONSerialization.jsonObject(with: data, options: .allowFragments) as? [[String: Any]]
                            var results = [SearchTextFieldItem]()
                            if let resultList = (jsonData as? [[String:Any]]) {
                                for result in resultList {
                                    print("4.ilterAddressInBackground result \(result)" )

                                    let placeId = result["place_id"] as? String
                                    let osmType = result["osm_type"] as? String
                                    let osmId   = result["osm_id"] as? String
                                    let lat     = result["lat"] as? Double
                                    let lon     = result["lon"] as? Double
                                    let name    = result["display_name"] as? String
                                    let place = SearchPlace(placeId: placeId, osmType: osmType, osmId: osmId, lat: lat, lon: lon, name: name, address: name)
                                    self.searchPlaceList.append(place)
                                    
                                    results.append(SearchTextFieldItem(title:name ?? "", subtitle:criteria.uppercased(), image: UIImage(named: "marker")))
                                }
                            }
                            
                            DispatchQueue.main.async {
                                callback(results)
                            }

                        }else{
                            DispatchQueue.main.async {
                                callback([])
                            }
                        }
                    }catch{
                        print("Network error : \(error)")
                        DispatchQueue.main.async {
                            callback([])
                        }
                    }
                })
                task.resume()
            }
        }
    
    fileprivate func geoCodingInBackground(_ latitude: Double, _ longitude: Double) {
            let searchUrl = URL(string : "https://api.pizzastudio.app/api-server/api/place/reverse?lat=\(latitude)&lon=\(longitude)" )
            print("1.geoCodingInBackground called \(searchUrl)" )
            
            if let url = searchUrl {
                var request = URLRequest(url:url)
                let accessToken = UserDefaults.standard.string(forKey: "accessToken") as! String
                print("2.geoCodingInBackground acessToken \(accessToken)" )
                request.setValue("Bearer \(accessToken)" , forHTTPHeaderField: "Authorization")
//                request.setValue("Bearer \(accessToken)" , forHTTPHeaderField: "Authorization")
                let task = URLSession.shared.dataTask(with: request, completionHandler: {(data, response, error) in
                    do {
                        if let data = data {
                            print("3.geoCodingInBackground data \(data)" )
//                            let jsonData = try JSONSerialization.jsonObject(with: data, options:.allowFragments) as? [[String:Any]]
                            let jsonData = try JSONSerialization.jsonObject(with: data, options:.allowFragments)
                            print("4.geoCodingInBackground jsonData \(jsonData)")
                            if jsonData != nil {
                                if let resultList = (jsonData as? [[String:Any]]) {
                                    if let result = resultList.first {
                                        let placeId = result["place_id"] as? String
                                        let osmType = result["osm_type"] as? String
                                        let osmId   = result["osm_id"] as? String
                                        let lat     = result["lat"] as? Double
                                        let lon     = result["lon"] as? Double
                                        let name    = result["display_name"] as? String
                                        let place = SearchPlace(placeId: placeId, osmType: osmType, osmId: osmId, lat: lat, lon: lon, name: name, address: name)
                                        self.savedSearchPlace = place;
                                        DispatchQueue.main.async {
                                            let shortAddr = place.name!.components(separatedBy: ",")
                                            if shortAddr.count > 0 {
                                                self.addressTextField.text = shortAddr[0]
                                            }else{
                                                self.addressTextField.text = place.name
                                            }
                                            
                                        }
                                    }
                                }
                            }
                            
                        }else{
                            let place = SearchPlace(placeId: "\(latitude):\(longitude)", osmType: "latlon", osmId: "\(latitude):\(longitude)", lat: latitude, lon: longitude, name: "\(latitude):\(longitude)", address:"\(latitude):\(longitude)")
                            self.savedSearchPlace = place;
                            DispatchQueue.main.async {
                                let shortAddr = place.name!.components(separatedBy: ",")
                                if shortAddr.count > 0 {
                                    self.addressTextField.text = shortAddr[0]
                                }else{
                                    self.addressTextField.text = place.name
                                }
                                
//                                self.addressTextField.text = place.name
                            }
                        }
                    }catch{
                        print("Network error : \(error)")
                        let place = SearchPlace(placeId: "\(latitude):\(longitude)", osmType: "latlon", osmId: "\(latitude):\(longitude)", lat: latitude, lon: longitude, name: "\(latitude):\(longitude)", address:"\(latitude):\(longitude)")
                        self.savedSearchPlace = place;
                        DispatchQueue.main.async {
//                            self.addressTextField.text = place.name
                            let shortAddr = place.name!.components(separatedBy: ",")
                            if shortAddr.count > 0 {
                                self.addressTextField.text = shortAddr[0]
                            }else{
                                self.addressTextField.text = place.name
                            }
                        }
                    }
                })
                task.resume()
            }
        }
    
}

extension FriendViewController
{
    func setupToHideKeyboardOnTapOnView()
    {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(
            target: self,
            action: #selector(FriendViewController.dismissKeyboard))

        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }

    @objc func dismissKeyboard()
    {
        view.endEditing(true)
    }
    
}

