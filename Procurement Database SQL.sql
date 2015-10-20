-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`product` (
  `product_ID` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `product_name` VARCHAR(45) NOT NULL COMMENT '',
  PRIMARY KEY (`product_ID`)  COMMENT '')
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`supplier`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`supplier` (
  `supplier_ID` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `supplier_name` VARCHAR(45) NOT NULL COMMENT '',
  `supplier_contactnumber` DOUBLE NOT NULL COMMENT '',
  `supplier_active` TINYINT(1) NOT NULL COMMENT '',
  PRIMARY KEY (`supplier_ID`)  COMMENT '')
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`productTag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`productTag` (
  `productTag_ID` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `productTag_name` VARCHAR(45) NOT NULL COMMENT '',
  `productTag_newprice` DOUBLE NOT NULL COMMENT '',
  `productTag_availability` TINYINT(1) NOT NULL COMMENT '',
  PRIMARY KEY (`productTag_ID`)  COMMENT '')
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`productList`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`productList` (
  `supplier_ID` INT NOT NULL COMMENT '',
  `product_ID` INT NOT NULL COMMENT '',
  `product_availability` TINYINT(1) NOT NULL COMMENT '',
  `productTag_productTag_ID` INT NOT NULL COMMENT '',
  PRIMARY KEY (`supplier_ID`, `product_ID`, `productTag_productTag_ID`)  COMMENT '',
  INDEX `fk_supplier_has_product_product1_idx` (`product_ID` ASC)  COMMENT '',
  INDEX `fk_supplier_has_product_supplier_idx` (`supplier_ID` ASC)  COMMENT '',
  INDEX `fk_productList_productTag1_idx` (`productTag_productTag_ID` ASC)  COMMENT '',
  CONSTRAINT `fk_supplier_has_product_supplier`
    FOREIGN KEY (`supplier_ID`)
    REFERENCES `mydb`.`supplier` (`supplier_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_supplier_has_product_product1`
    FOREIGN KEY (`product_ID`)
    REFERENCES `mydb`.`product` (`product_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_productList_productTag1`
    FOREIGN KEY (`productTag_productTag_ID`)
    REFERENCES `mydb`.`productTag` (`productTag_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`delivery`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`delivery` (
  `delivery_number` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `delivery_status` TINYINT(1) NOT NULL COMMENT '',
  `delivery_quantity` INT NOT NULL COMMENT '',
  `productList_supplier_ID` INT NOT NULL COMMENT '',
  `productList_product_ID` INT NOT NULL COMMENT '',
  PRIMARY KEY (`delivery_number`, `productList_supplier_ID`, `productList_product_ID`)  COMMENT '',
  INDEX `fk_delivery_productList1_idx` (`productList_supplier_ID` ASC, `productList_product_ID` ASC)  COMMENT '',
  CONSTRAINT `fk_delivery_productList1`
    FOREIGN KEY (`productList_supplier_ID` , `productList_product_ID`)
    REFERENCES `mydb`.`productList` (`supplier_ID` , `product_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`orderList`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`orderList` (
  `order_number` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `supplier_ID` INT NOT NULL COMMENT '',
  `delivery_number` INT NOT NULL COMMENT '',
  `order_date` DATE NOT NULL COMMENT '',
  `order_quantity` INT NOT NULL COMMENT '',
  `order_status` VARCHAR(45) NOT NULL COMMENT '',
  PRIMARY KEY (`order_number`, `supplier_ID`, `delivery_number`)  COMMENT '',
  INDEX `fk_order_supplier1_idx` (`supplier_ID` ASC)  COMMENT '',
  INDEX `fk_order_delivery1_idx` (`delivery_number` ASC)  COMMENT '',
  CONSTRAINT `fk_order_supplier1`
    FOREIGN KEY (`supplier_ID`)
    REFERENCES `mydb`.`supplier` (`supplier_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_delivery1`
    FOREIGN KEY (`delivery_number`)
    REFERENCES `mydb`.`delivery` (`delivery_number`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
