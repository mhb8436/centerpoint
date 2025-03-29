import UIKit
import SDWebImage

class RestrauntTableViewCell: UITableViewCell {

    var restraunt:RecommendPlaceModel? {
        didSet {
            guard let item = restraunt else {return}
            let id = item.id
            let title = item.title
            if !title.isEmpty {
                nameLabel.text = title
            }
            let score = item.score
            if !score.isNaN {
                scoreLabel.text = "\(score)"
            }
            let detail = item.detail
            let location = item.location
            if !location.isEmpty {
                addressLabel.text = location
            }
            let lat = item.lat
            let lng = item.lng
            let thumb_link = item.thumb_link
            let thumb_img = item.thumb_img
            if thumb_img != nil {
//                print(thumb_img)
                thumbImageView.sd_setImage(with: URL(string:thumb_img!), placeholderImage: UIImage(named: "marker.png")) {(image, error, cache, urls) in
//                    print(error, image, cache, urls)
                    if (error != nil) {
                        self.thumbImageView.image = UIImage(named: "marker.png")
                    } else {
                        self.thumbImageView.image = image
                    }

                }
            }
//            print("RestrauntTableViewCell", "didSet", detail)
        }
    }
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?){
        super.init(style: style, reuseIdentifier: reuseIdentifier);
        
        self.contentView.addSubview(thumbImageView)
        self.containerView.addSubview(nameLabel)
        self.containerView.addSubview(scoreLabel)
        self.containerView.addSubview(addressLabel)
        self.contentView.addSubview(containerView)
        
        thumbImageView.centerYAnchor.constraint(equalTo: self.contentView.centerYAnchor).isActive = true
        thumbImageView.leadingAnchor.constraint(equalTo: self.contentView.leadingAnchor, constant: 10).isActive = true
        thumbImageView.widthAnchor.constraint(equalToConstant: 40).isActive = true
        thumbImageView.heightAnchor.constraint(equalToConstant: 40).isActive = true
        
        
        containerView.centerYAnchor.constraint(equalTo: self.contentView.centerYAnchor).isActive = true
        containerView.leadingAnchor.constraint(equalTo: self.thumbImageView.trailingAnchor, constant: 10).isActive = true
        containerView.trailingAnchor.constraint(equalTo:self.contentView.trailingAnchor, constant: -10).isActive = true
        containerView.heightAnchor.constraint(equalToConstant: 60).isActive = true
        
        
        addressLabel.topAnchor.constraint(equalTo: self.containerView.topAnchor).isActive = true
        addressLabel.leadingAnchor.constraint(equalTo:self.containerView.leadingAnchor).isActive = true
        addressLabel.trailingAnchor.constraint(equalTo: self.containerView.trailingAnchor).isActive = true
        
        nameLabel.topAnchor.constraint(equalTo:self.addressLabel.bottomAnchor).isActive = true
        nameLabel.centerYAnchor.constraint(equalTo:self.containerView.centerYAnchor).isActive = true
        nameLabel.leadingAnchor.constraint(equalTo:self.containerView.leadingAnchor).isActive = true
        nameLabel.trailingAnchor.constraint(equalTo:self.containerView.trailingAnchor).isActive = true
        
        scoreLabel.topAnchor.constraint(equalTo: self.nameLabel.bottomAnchor).isActive = true
        scoreLabel.leadingAnchor.constraint(equalTo: self.containerView.leadingAnchor).isActive = true
        
    }
    
    required init?(coder aDecoder: NSCoder){
        super.init(coder: aDecoder)
    }
    
    let containerView:UIView = {
        let view = UIView()
        view.translatesAutoresizingMaskIntoConstraints = false
        view.clipsToBounds = true
        return view
    }()
    
    
    let thumbImageView:UIImageView = {
        let img = UIImageView()
        img.contentMode = .scaleAspectFill
        img.translatesAutoresizingMaskIntoConstraints = false
        img.layer.cornerRadius = 0
        img.clipsToBounds = true
        img.image = UIImage(named: "marker")
        return img
    }()
    
    
    let nameLabel:UILabel = {
        let label = UILabel()
        label.font = UIFont.boldSystemFont(ofSize: 20)
//        label.textColor = .black
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    let addressLabel:UILabel = {
        let label = UILabel()
        label.font = UIFont.boldSystemFont(ofSize: 14)
//        label.textColor = .gray
        label.layer.cornerRadius = 0
        label.clipsToBounds = true
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    let scoreLabel:UILabel = {
        let label = UILabel()
        label.font = UIFont.systemFont(ofSize: 12)
        label.textColor = .red
        label.layer.cornerRadius = 5
        label.clipsToBounds = true
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
}
