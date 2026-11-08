package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random
import com.example.myapplication.R

// ==========================================
// ЗАДАНИЕ 1: Функция Hello
// ==========================================
@Composable
fun Hello(name: String?) {
    val textToDisplay = if (name == null) "Имя не задано" else "Привет, $name!"
    Text(text = textToDisplay, fontSize = 24.sp, modifier = Modifier.padding(16.dp))
}

// ==========================================
// ЗАДАНИЕ 2: Превью для Hello
// ==========================================
@Preview(name = "Portrait", device = "spec:width=360dp,height=640dp")
@Preview(name = "Landscape", device = "spec:width=640dp,height=360dp")
@Preview(name = "Round", device = "spec:shape=Round,width=200dp,height=200dp,dpi=240", showBackground = true, backgroundColor = 0xFFFFFF00)
@Composable
fun HelloPreview() {
    Hello("Анна")
}

// ==========================================
// ЗАДАНИЕ 3: Работа со строками и стилями
// ==========================================
@Preview
@Composable
fun TextStylesPreview() {
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.compose_desc),
            color = Color.Green,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = stringResource(id = R.string.compose_desc),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = stringResource(id = R.string.compose_desc),
            fontSize = 24.sp,
            color = Color.Black,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .padding(top = 48.dp)
                .background(Color.Green)
                .padding(8.dp)
        )
    }
}

// ==========================================
// ЗАДАНИЕ 4: Кастомная кнопка
// ==========================================
@Preview
@Composable
fun CustomButtonPreview() {
    Button(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
            .border(2.dp, Color.Gray, RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Text(text = "Нажми меня", color = Color.Black, fontSize = 18.sp)
    }
}

// ==========================================
// ЗАДАНИЕ 5: Контейнер с инициалами
// ==========================================
@Preview
@Composable
fun InitialsContainerPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 16.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "КА", color = Color.Black, fontSize = 20.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        }
    }
}

// ==========================================
// ЗАДАНИЕ 10: Исправленная корзина
// ==========================================
data class Cart(val products: List<Product>)
data class Product(val id: Int, val name: String, val price: Int)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingCartScreen()
        }
    }

    @Composable
    fun ShoppingCartScreen() {
        var products by remember {
            mutableStateOf(
                listOf(
                    Product(0, "Товар #1", 100),
                    Product(1, "Товар #2", 150),
                    Product(2, "Товар #3", 56)
                )
            )
        }

        val totalSum = products.sumOf { it.price }
        val productSize = products.size
        val context = LocalContext.current

        LaunchedEffect(totalSum) {
            if (totalSum > 500) {
                Toast.makeText(context, "Доставка бесплатная!", Toast.LENGTH_SHORT).show()
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            for (product in products) {
                Text(text = "${product.name} - ${product.price} рублей")
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Товаров на сумму: $totalSum рублей", fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)

            AddProductSection(onAdd = {
                products = products + Product(
                    id = products.size,
                    name = "Товар #${products.size + 1}",
                    price = Random.nextInt(0, 100)
                )
            })

            RemoveProductSection(
                isVisible = productSize > 0,
                onRemove = {
                    if (products.isNotEmpty()) {
                        products = products.dropLast(1)
                    }
                }
            )
        }
    }

    @Composable
    fun AddProductSection(onAdd: () -> Unit) {
        Button(onClick = onAdd, modifier = Modifier.padding(top = 8.dp)) {
            Text(text = "Добавить товар")
        }
    }

    @Composable
    fun RemoveProductSection(isVisible: Boolean, onRemove: () -> Unit) {
        if (isVisible) {
            Button(onClick = onRemove, modifier = Modifier.padding(top = 8.dp)) {
                Text(text = "Удалить товар")
            }
        }
    }
}