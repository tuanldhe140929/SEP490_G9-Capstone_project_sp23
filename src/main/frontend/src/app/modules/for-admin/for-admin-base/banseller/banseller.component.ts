import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { AccountService } from 'src/app/services/account.service';
import { AddviolationService } from 'src/app/services/addviolation.service';

@Component({
  selector: 'app-banseller',
  templateUrl: './banseller.component.html',
  styleUrls: ['./banseller.component.css']
})
export class BansellerComponent implements OnInit{
  constructor(@Inject(MAT_DIALOG_DATA) public data: {id: number, email: string, enabled: boolean}, private vioService: AddviolationService, private toastr: ToastrService){}

  formerStatus: boolean = this.data.enabled;

  ngOnInit(): void {
    console.log(this.data.id);
  }

  onBanSeller(){
    this.vioService.updateSellerStatus(this.data.id).subscribe(
      data => {
        console.log(data);
        if(this.formerStatus){
          this.toastr.success("Tắt tài khoản thành công!");
        }else{
          this.toastr.success("Bật tài khoản thành công!");
        }
      }
    )
  }

}
