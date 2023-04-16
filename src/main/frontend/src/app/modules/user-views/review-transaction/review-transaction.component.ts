import { OnInit, ViewChild } from '@angular/core';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Transaction, TransactionStatus } from '../../../dtos/Transaction';
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
        
        this.transaction = data;
        for (let i = 0; i < this.transaction.cart.items.length; i++) {
          this.transaction.cart.items[i].productDetails.price = Number.parseFloat(this.transaction.cart.items[i].productDetails.price.toFixed(2));
        }
        this.caculateFee();
        if (this.transaction.change) {
          this.info = "Có sản phẩm không còn khả dụng trong giỏ hàng, giao dịch này sẽ bị hủy";
          this.openInfoModal();
          this.transaction.status = TransactionStatus.CANCELED;
          return;
        }
      },
      error => {
        console.log(error);
      })
  }

  caculateFee(): void {
    if (this.transaction.id != -1) {
     
      var feePercentage = this.transaction.fee.percentage;
      this.originalPrice = this.transaction.amount;
      this.fee =  this.originalPrice /(100+feePercentage) * 10;
		  this.fee = parseFloat(this.fee.toFixed(2));
	
      this.originalPrice = Math.round(this.originalPrice * 100) / (100+feePercentage);
      this.originalPrice = parseFloat(this.originalPrice.toFixed(2))
      this.fee = Math.round(this.fee * 100) / 100;
      this.total = this.originalPrice + this.fee;
      this.total = parseFloat(this.total.toFixed(2));
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
        this.isLoading = false;
        this.transaction = data;
        if (this.transaction.change) {
          this.info = "Có sản phẩm không còn khả dụng trong giỏ hàng, giao dịch này đã bị hủy";
          this.transaction.status = TransactionStatus.CANCELED;
          this.openInfoModal();
        }
        switch (data.status) {
          case TransactionStatus.EXPIRED:
            this.info = "Giao dịch hết hạn";
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
        this.isLoading = false;
        this.info = "Không thể thực hiện hành động này";
        this.openInfoModal();
        console.log(error);
      }
    );
  }

  cancelPayment() {
    this.isLoading = true;
    this.transactionService.cancelPayment(this.transaction.id).subscribe(
      data => {
		  this.isLoading = false;
        this.transaction = data;
        if(data.status == TransactionStatus.EXPIRED){
			this.info = "Giao dịch hết hạn";

		}else{
			this.info = "Hủy thanh toán thành công";

		}
		 this.openInfoModal();
      },
      error => {
        this.isLoading = false;
        this.info = "Không thể thực hiện hành động này";
        this.openInfoModal();
        console.log(error);
      }
    );
  }

  get TransactionStatus() {
    return TransactionStatus;
  }
}
