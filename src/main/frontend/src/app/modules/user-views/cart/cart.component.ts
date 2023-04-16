import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Cart } from 'src/app/dtos/Cart';
import { CartItem } from 'src/app/dtos/CartItem';
import { Product } from 'src/app/dtos/Product';
import { Transaction, TransactionStatus } from 'src/app/dtos/Transaction';
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
    private modalService: NgbModal,
    private router: Router) {

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
        for (let i = 0; i < data.items.length; i++) {
          data.items[i].product.price = Number.parseFloat(data.items[i].product.price.toFixed(1));
        }
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
        this.isLoading = false;
        this.transaction = data;
        if (this.transaction.change) {
          this.info = "Có sản phẩm không còn khả dụng trong giỏ hàng";
          this.openInfoModal();
          this.getAllCartItem();
        }
        if (this.transaction.amount > 0) {
          var strWindowFeatures = "location=yes,height=570,width=520,scrollbars=yes,status=yes";
          var URL = data.approvalUrl;
          window.open(URL, "_blank", strWindowFeatures);
        }

        this.fetchTransactionStatus();
      },
      error => {
        this.isLoading = false;
        this.info = "Không thể thực hiện hành động";
        this.openInfoModal();
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

    return this.cart.totalPrice.toFixed(2);
  }
  openInfoModal() {
    this.modalService.open(this.infoModal, { centered: true });
  }
  dismiss() {
    window.location.reload();
    this.modalService.dismissAll();
  }

  toViewProduct(item: CartItem) {
    this.router.navigate(['products/' + item.product.id]);
  }

  get Fee() {
    const fee = Number.parseFloat(this.TotalPrice) / 10;
    return fee.toFixed(2);
  }

  get LastPrice() {
    const lastPrice = Number.parseFloat(this.Fee) + Number.parseFloat(this.TotalPrice);
    return lastPrice.toFixed(2);
  }
}
