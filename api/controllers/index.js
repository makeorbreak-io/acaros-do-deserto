'use strict';
const express = require('express'),
	router = express.Router(),
	Sequelize = require('sequelize'),
	sequelize = new Sequelize('products', 'postgres', 'admin', {host: 'localhost', port:'5432', dialect: 'postgres'}),
	dbPopulator = require('./populate_db');

const Product = sequelize.define('products', {
	id: {type: Sequelize.INTEGER, primaryKey: true, autoIncrement: true},
	barcode: {type: Sequelize.STRING(50), allowNull: false, unique: true},
	name: {type: Sequelize.STRING(150), allowNull: false}
});

const PendingProduct = sequelize.define('pending', {
	id: {type: Sequelize.INTEGER, primaryKey: true, autoIncrement: true},
	barcode: {type: Sequelize.STRING(50), allowNull: false, unique: true},
	name: {type: Sequelize.STRING(150), allowNull: false},
	ingredients: {type: Sequelize.STRING(5000), allowNull: false},
	device: {type: Sequelize.STRING(100), allowNull: false},
	shop: {type: Sequelize.STRING(150), allowNull: false}
});

const Ingredient = sequelize.define('ingredients', {
	id: {type: Sequelize.INTEGER, primaryKey: true, autoIncrement: true},
	name: {type: Sequelize.STRING(150), allowNull: false, unique: true},
	description: {type: Sequelize.STRING(250), allowNull: false},
	danger: {type: Sequelize.STRING(2), allowNull: false},
	allergen: {type: Sequelize.BOOLEAN, allowNull: false}
});

const ProductIngredient = sequelize.define('productIngredient', {
	productId: {type: Sequelize.INTEGER, primaryKey: true, references: {
		model: Product,
		key: 'id'
	}},
	ingredientId: {type: Sequelize.INTEGER, primaryKey: true, references: {
		model: Ingredient,
		key: 'id'
	}}
});

const ProductReport = sequelize.define('productReport', {
	id: {type: Sequelize.INTEGER, primaryKey: true, autoIncrement: true},
	productId: {type: Sequelize.INTEGER, allowNull: false, references: {
		model: Product,
		key: 'id'
	}},
	reason: {type: Sequelize.STRING(1024), allowNull: false},
	device: {type: Sequelize.STRING(100), allowNull: false}
});

router.get('/dbreset', function(req, res) {
	res.send('Resetting the database....');

	sequelize.sync({
		force: true
	})
	.then(() => Product.bulkCreate(dbPopulator.defaultProducts))
	.then(() => Ingredient.bulkCreate(dbPopulator.defaultIngredients))
	.then(() => ProductIngredient.bulkCreate(dbPopulator.defaultProductIngredients))
	.catch((err) => console.log(err));
});

router.get('/api/product/:barcode', function(req, res) {
	var ingredientNames = [];
	var requestedProduct;

	Product.findOne({where: {barcode: req.params.barcode}})
	.then((product) => {
		requestedProduct = product;

		if (product !== null) {
			return ProductIngredient.findAll({where: {productId: product.id}})
			.then((productIngredients) => {
				for (var i = 0; i < productIngredients.length; ++i) {
					var ingredientId = productIngredients[i].ingredientId;
					
					return Ingredient.findOne({where: {id: ingredientId}})
					.then((ingredient) => ingredientNames.push(ingredient.name));
				}
			});
		} else {
			res.status(404).send('404 Not Found');
			return Promise.reject();
		}
	})
	.then (() => {
		requestedProduct = requestedProduct.toJSON();
		requestedProduct.ingredients = ingredientNames;
		
		res.json(requestedProduct);
	});
});

router.get('/api/addpendingproduct', function(req, res) {
	PendingProduct.create({
		barcode: req.query.barcode,
		name: req.query.name,
		ingredients: req.query.ingredients,
		device: req.query.device,
		shop: req.query.shop
	}).then((pendingProduct) => {
		if (pendingProduct !== null) {
			res.json(pendingProduct);
		}
		else {
			res.status(400).send('400 Bad Request');
		}
	});
});

router.get('/api/addproduct', function(req, res) {
	Product.create({
		barcode: req.query.barcode,
		name: req.query.name,
	}).then((product) => {
		if (product !== null) {
			res.json(product);
		}
		else {
			res.status(400).send('400 Bad Request');
		}
	});
});

router.get('/api/reportproduct', function(req, res) {
	ProductReport.create({
		id: req.query.id,
		productId: req.query.productId,
		reason: req.query.reason,
		device: req.query.device
	});
});

router.get('/api/ingredients', function(req, res) {
	Ingredient.findAll().then((ingredients) => {
		res.json(ingredients);
	});
});

router.get('/api/allergens', function(req, res) {
	Ingredient.findAll({where: {allergen: true}}).then((allergens) => {
		res.json(allergens);
	});
});

router.get('/api/ingredient/:name', function(req, res) {
	Ingredient.findOne({where: {name: req.params.name}}).then(
		(ingredient) => {
			if (ingredient !== null) {
				res.json(ingredient);
			} else {
				res.status(404).send('404 Not Found');
			}
		});
});

router.get('/api/addingredient', function(req, res) {
	Ingredient.create({
		name: req.query.name,
		description: req.query.description,
		danger: req.query.danger
	})
	.then((ingredient) => {
		if (ingredient !== null) {
			res.json(ingredient);
		}
		else {
			res.status(400).send('400 Bad Request');
		}
	});
});

module.exports = router;
