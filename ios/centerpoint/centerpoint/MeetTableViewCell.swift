//
//  MeetTableViewCell.swift
//  centerpoint
//
//  Created by ethan.lee781 on 14/03/2020.
//  Copyright © 2020 ethan.lee781. All rights reserved.
//

import UIKit


class MeetTableViewCell: UITableViewCell {

    var meet:Meet? {
        didSet {
            guard let meetItem = meet else {return}
//            dotImageView.image = UIImage(named:  "dot")
            let name = meetItem.name
            if !name.isEmpty {
                nameLabel.text = name
            }
            let createDate : Date? = meetItem.date
            if  createDate != nil {
                let formatter = DateFormatter()
                formatter.dateFormat = "yyyy-MM-dd"
                let dateStr = formatter.string(from: createDate ?? Date())
                createDateLabel.text = dateStr
            }
            let countDetailed : Int = meetItem.count
            countDetailedLabel.text = String(countDetailed)
            print(name + "------>")
        }
    }
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?){
        super.init(style: style, reuseIdentifier: reuseIdentifier);
        
        self.contentView.addSubview(dotImageView)
        containerView.addSubview(createDateLabel)
        containerView.addSubview(nameLabel)
        containerView.addSubview(countDetailedLabel)
        self.contentView.addSubview(containerView)
        
        dotImageView.centerYAnchor.constraint(equalTo:self.contentView.centerYAnchor).isActive = true
        dotImageView.leadingAnchor.constraint(equalTo:self.contentView.leadingAnchor, constant: 10).isActive = true
        dotImageView.widthAnchor.constraint(equalToConstant: 40).isActive = true
        dotImageView.heightAnchor.constraint(equalToConstant: 40).isActive = true
        
        containerView.centerYAnchor.constraint(equalTo: self.contentView.centerYAnchor).isActive = true
        containerView.leadingAnchor.constraint(equalTo: self.dotImageView.trailingAnchor, constant: 10).isActive = true
        containerView.trailingAnchor.constraint(equalTo:self.contentView.trailingAnchor, constant: -10).isActive = true
        containerView.heightAnchor.constraint(equalToConstant: 60).isActive = true
        
        createDateLabel.topAnchor.constraint(equalTo: self.containerView.topAnchor).isActive = true
        createDateLabel.leadingAnchor.constraint(equalTo: self.containerView.leadingAnchor).isActive = true
        createDateLabel.trailingAnchor.constraint(equalTo:self.containerView.trailingAnchor).isActive = true
        
        nameLabel.topAnchor.constraint(equalTo: self.createDateLabel.bottomAnchor).isActive = true
        nameLabel.centerYAnchor.constraint(equalTo:self.containerView.centerYAnchor).isActive = true
        nameLabel.leadingAnchor.constraint(equalTo: self.containerView.leadingAnchor).isActive = true
        nameLabel.trailingAnchor.constraint(equalTo:self.containerView.trailingAnchor).isActive = true

        countDetailedLabel.topAnchor.constraint(equalTo: self.nameLabel.bottomAnchor).isActive = true
        countDetailedLabel.leadingAnchor.constraint(equalTo: self.containerView.leadingAnchor).isActive = true
        
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
    
    let dotImageView:UIImageView = {
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
    
    let countDetailedLabel:UILabel = {
        let label = UILabel()
        label.font = UIFont.boldSystemFont(ofSize: 14)
        label.textColor = .red
        label.layer.cornerRadius = 5
        label.clipsToBounds = true
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    let createDateLabel:UILabel = {
        let label = UILabel()
        label.font = UIFont.systemFont(ofSize:12)
//        label.textColor = .gray
        label.layer.cornerRadius = 5
        label.clipsToBounds = true
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
}
