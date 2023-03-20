import { OnInit, ViewChild } from '@angular/core';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Transaction, TransactionStatus } from '../../../DTOS/Transaction';
import { TransactionService } from '../../../services/transaction.service';

@Component({
  selector: 'app-review-transaction',
  templateUrl: './review-transaction.component.html',
  styleUrls: ['./review-transaction.component.css']
})
export class ReviewTransactionComponent implements OnInit {
  @ViewChild('infoModal', { static: false }) private infoModal: any;
  isLoading = false;
  info = "";
  fee: number;
  originalPrice: number;
  transaction: Transaction = new Transaction();
  total: number;
  paymentId: string;
  payerId: string;
  token: string;
  constructor(private route: ActivatedRoute,
    private transactionService: TransactionService,
    private modalService: NgbModal  ) {

  }
  ngOnInit(): void {
    this.paymentId = this.route.snapshot.queryParams['paymentId'];
    this.token = this.route.snapshot.queryParams['token'];
    this.payerId = this.route.snapshot.queryParams['PayerID'];
    const $request = this.transactionService.reviewTransaction(this.paymentId, this.token, this.payerId);
    $request.subscribe(
      data => {
        console.log(data);
        this.transaction = data;
        this.caculateFee();
      },
      error => {
        console.log(error);
      })
  }

  caculateFee(): void {
    if (this.transaction.id != -1) {
     
      var feePercentage = this.transaction.fee.percentage;
      this.originalPrice = this.transaction.amount;
      this.fee =  this.originalPrice /100 * 10;

      this.originalPrice = Math.round(this.originalPrice * 100) / 100;
      this.fee = Math.round(this.fee * 100) / 100;
      this.total = this.originalPrice + this.fee;
    }
  }

  openInfoModal() {
    this.modalService.open(this.infoModal, { centered: true });
  }
  dismiss() {
    this.modalService.dismissAll();
  }

  executePayment() {
    this.isLoading = true;
    this.transactionService.executePayment(this.paymentId, this.payerId).subscribe(
      data => {

        switch (data.status) {
          case TransactionStatus.CREATED:
            console.log('created');
            break;
          case TransactionStatus.APPROVED:
            console.log('APPROVED');
            break;
          case TransactionStatus.FAILED:
            this.isLoading = false;
            this.info = "Thanh toán không thành công";
            console.log('FAILED');
            break;
          case TransactionStatus.COMPLETED:
            this.isLoading = false;
            this.info = "Thanh toán thành công";
            console.log('COMPLETED');
            break;
          case TransactionStatus.CANCELED:
            this.info = "Thanh toán đã bị hủy";
            console.log('CANCELED');
            break;
        }
        this.openInfoModal();
      },
      error => {
        this.info = "Thanh toán không thành công";
        this.openInfoModal();
      }
    );
  }

  cancelPayment() {
    this.isLoading = true;
    this.transactionService.cancelPayment(this.transaction.id).subscribe(
      data => {
        this.isLoading = false;
        if (data == true) {
          this.info = "Hủy thanh toán thành công";
          this.openInfoModal();
        }
      },
      error => {
        this.info = "Hủy thanh toán không thành công";
        this.openInfoModal();
      }
    );
  }
}
