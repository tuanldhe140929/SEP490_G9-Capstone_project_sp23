
import { Component } from '@angular/core';
import { Payout } from 'src/app/DTOS/Payout';
import { PayoutService } from 'src/app/services/payout.service';

@Component({
  selector: 'app-payout-history',
  templateUrl: './payout-history.component.html',
  styleUrls: ['./payout-history.component.css']
})
export class PayoutHistoryComponent {
  payoutlist: Payout[] = [];
  constructor(
    private payoutService: PayoutService
  ){}
 
  ngOnInit(): void {
    this.getPayoutHistory();
 } 

 getPayoutHistory(){
  this.payoutService.getPayoutHistory().subscribe(
    data =>{
      console.log(data);
      this.payoutlist = data;
    }
  )
 }
}