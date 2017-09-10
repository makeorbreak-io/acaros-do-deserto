'use strict';

module.exports.defaultIngredients = [
	{
		name: 'Glúten',
		description: 'O glúten resulta da mistura de proteínas que se encontram naturalmente no endosperma da semente de cereais da família das gramíneas, subfamília Pooideae, principalmente das espécies da tribo Triticeae, como o trigo, cevada, triticale e centeio.',
		danger: 'L',
		allergen: true
	},
	{
		name: 'Aveia',
		description: 'Avena é um género botânico pertencente à família Poaceae, subfamília Pooideae, tribo Aveneae. O gênero é composto por aproximadamente 450 espécies. As espécies de Avena mais cultivadas são Avena sativa e Avena byzantina.',
		danger: 'L',
		allergen: true
	},
	{
		name: 'Trigo',
		description: 'O trigo é uma gramínea cultivada em todo o mundo. Mundialmente, é a segunda maior cultura de cereais, a seguir ao milho; a terceira é o arroz.',
		danger: 'L',
		allergen: true
	},
	{
		name: 'Leite',
		description: 'Leite é uma secreção nutritiva de cor esbranquiçada e opaca produzida pelas glândulas mamárias das fêmeas dos mamíferos. O líquido é produzido pelas células secretoras das glândulas mamárias ou mamas.',
		danger: 'L',
		allergen: true
	},
	{
		name: 'Água',
		description: 'Água é uma substância química cujas moléculas são formadas por dois átomos de hidrogênio e um de oxigênio. É abundante no Universo, inclusive na Terra, onde cobre grande parte de sua superfície e é o maior constituinte dos fluidos dos seres vivos.',
		danger: 'L',
		allergen: false
	},
	{
		name: 'Etoxiquina',
		description: 'Etoxiquina é um antioxidante utilizado em rações produzidas para animais. Tem função de conservante. Segurança e eficácia da etoxiquina para todas as espécies animais',
		danger: 'VD',
		allergen: false
	}
];

module.exports.defaultProducts = [
	{
		id: 1337,
		barcode: '4PU',
		name: 'Ah-poo'
	}
];

module.exports.defaultProductIngredients = [
	{
		productId: 1337,
		ingredientId: 2
	}
];
