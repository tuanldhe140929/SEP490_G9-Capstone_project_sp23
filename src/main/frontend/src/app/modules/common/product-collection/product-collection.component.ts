import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { User } from '../../../DTOS/User';
import { CommonService } from '../../../services/common.service';

@Component({
  selector: 'app-product-collection',
  templateUrl: './product-collection.component.html',
  styleUrls: ['./product-collection.component.css']
})
export class ProductCollectionComponent implements OnInit {
  user: User = new User();
  products: any[] = [];
  message = '';
  message2 = '';
  constructor(private commonService: CommonService,
    private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.getUserInfo();
  }

  getUserInfo() {
    var username = this.activatedRoute.snapshot.paramMap.get('username')?.toString();
    if (username != null) {
      this.commonService.getUserInfoByUsername(username).subscribe(
        (data) => {
          this.user = data;
          this.getProductByUser(this.user.username);
        },
        (err) => {
          this.message = "Khong tim thay user nay";
        });
    }
  }

  getProductByUser(username: string) {
    this.commonService.getProductsByUsername(username).subscribe(
      (data) => { this.products = data; },
      (err) => { this.message2 = "User nay khong co product nao"; });
  }
}
