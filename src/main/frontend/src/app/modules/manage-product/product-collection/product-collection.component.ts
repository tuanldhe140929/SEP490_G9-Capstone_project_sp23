import { ActivatedRoute, ParamMap } from '@angular/router';
import { ManageProductService } from 'src/app/services/manage-product.service';
import { StorageService } from 'src/app/services/storage.service';
import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/DTOS/User';
@Component({
  selector: 'app-product-collection',
  templateUrl: './product-collection.component.html',
  styleUrls: ['./product-collection.component.css']
})
export class ProductCollectionComponent {
  owner: User = new User();
  constructor(
    private storageService: StorageService,
    private manageProductService: ManageProductService,
    private route: ActivatedRoute
  ) {

  }
//   OnInit() {
//     this.route.paramMap.subscribe((params:
//       ParamMap) => {
//         console.log.
        
//       }
//       )
// }
//     this.getOwner(this.owner.username)
//   }


//   getOwner(username: string) {
//     this.manageProductService.getCurrentOwnerInfo(username).subscribe(
//       data => {
//         this.owner = data;
//         console.log(data)
//       },
//       error => {
//         console.log(error)
//       }
//     )
//   }

//   getVisitor(username: string) {
//     if (this.storageService.isLoggedIn()) {
//       //
//     }
//   }



}
