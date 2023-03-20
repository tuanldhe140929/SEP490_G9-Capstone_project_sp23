import { Component, OnInit, ViewChild } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Cart } from 'src/app/DTOS/Cart';
import { CartItem } from 'src/app/DTOS/CartItem';
import { Product } from 'src/app/DTOS/Product';
import { Transaction, TransactionStatus } from 'src/app/DTOS/Transaction';
import { CartService } from 'src/app/services/cart.service';
import { TransactionService } from 'src/app/services/transaction.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
})
export class CartComponent implements OnInit {

  @ViewChild('infoModal', { static: false }) private infoModal: any;

  isLoading = false;
  info = "";
  cart: Cart = new Cart;
  cartItemList: CartItem[] = [];
  itemname: any;
  itemprice: any;
  transaction: Transaction = new Transaction;

  constructor(private cartService: CartService,
    private transactionService: TransactionService,
    private modalService: NgbModal  ) {

  }
  ngOnInit(): void {
    this.getAllCartItem();
  }
  public getAllCartItem() {
    this.cartService.getAllProduct().subscribe(
      data => {
        console.log(data);
        this.cart = data;
        this.cartItemList = data.items;
      },
      (error) => {
        console.log(error);
      }
    )
  }
  public getItemCoverImageSrc(cartItem: CartItem) {
    if (cartItem != null && cartItem.product.id != -1 && cartItem.product.coverImage != null) {

      return 'http://localhost:9000/public/serveMedia/image?source=' + cartItem.product.coverImage.replace(/\\/g, '/');
    }
    else {
      return "";
    }
  }

  public RemoveItem(cartItem: CartItem): void {

    this.cartService.removeItem(cartItem).subscribe(
      (data) => {
        this.cart = data;
        this.cartItemList = data.items;
      },
      (error) => {
        console.log(error);
      }
    )
    console.log('xóa thành công ' + cartItem.product.id)
    this.RemoveItem === null
  }
  public checkout(): void {
    this.isLoading = true;
    this.transactionService.purchase(this.cart.id).subscribe(
      data => {
        this.transaction = data;
        if (this.transaction.amount > 0) {
          var strWindowFeatures = "location=yes,height=570,width=520,scrollbars=yes,status=yes";
          var URL = data.approvalUrl;
          window.open(URL, "_blank", strWindowFeatures);
        } else {

        }
        this.fetchTransactionStatus();
      },
      error => {
        console.log(error);
      }
    )
  }
  fetchTransactionStatus() {
    const checkStatus = () => {
      this.transactionService.checkTransactionStatus(this.transaction.id).subscribe(
        data => {
          this.isLoading = false;
          this.transaction.status = data;
          switch (data) {
            case TransactionStatus.CREATED:
              setTimeout(checkStatus, 3000);
              break;
            case TransactionStatus.APPROVED:
              setTimeout(checkStatus, 3000);
              break;
            case TransactionStatus.FAILED:
              this.info = "Thanh toán không thành công";
              this.openInfoModal();
              break;
            case TransactionStatus.COMPLETED:
              this.getAllCartItem();
              this.info = "Thanh toán thành công";
              this.openInfoModal();
              break;
            case TransactionStatus.CANCELED:
              this.info = "Bạn đã hủy thanh toán";
              this.openInfoModal();
              break;
          }

        },
        error => {
          console.log(error);
        }
      )
    };
    checkStatus();
  }



  get TotalPrice() {
    return this.cart.totalPrice;
  }
  openInfoModal() {
    this.modalService.open(this.infoModal, { centered: true });
  }
  dismiss() {
    this.modalService.dismissAll();
  }

}
