import { User } from "./User";

export class Seller extends User{
  phoneNumber: String;
  sellerEnabled: boolean;
  constructor() {
    super();
    this.phoneNumber = "";
    this.sellerEnabled = true;
  }
}
