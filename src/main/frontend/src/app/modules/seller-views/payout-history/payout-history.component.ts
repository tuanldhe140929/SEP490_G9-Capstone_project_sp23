import { StorageService } from 'src/app/services/storage.service';
import { AuthResponse } from 'src/app/dtos/AuthResponse';

import { Component } from '@angular/core';
import { Payout } from 'src/app/dtos/Payout';
import { PayoutService } from 'src/app/services/payout.service';


@Component({
  selector: 'app-payout-history',
  templateUrl: './payout-history.component.html',
  styleUrls: ['./payout-history.component.css']
})
export class PayoutHistoryComponent {
  authResponse: AuthResponse = new AuthResponse;
  payoutlist: Payout[] = [];
  constructor(
    
    private storageService: StorageService,
    private payoutService: PayoutService,
  
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
 formatTime(createdDate: Date){
const timestamp = createdDate;
const date = new Date(timestamp);

const formattedDate = date.getDate().toString().padStart(2, '0') + '-' +
                      (date.getMonth() + 1).toString().padStart(2, '0') + '-' +
                      date.getFullYear().toString();

const formattedTime = date.getHours().toString().padStart(2, '0') + ':' +
                      date.getMinutes().toString().padStart(2, '0') + ':' +
                      date.getSeconds().toString().padStart(2, '0');

const formattedTimestamp = formattedDate + ' ' + formattedTime;
console.log(formattedTimestamp);
 }
}