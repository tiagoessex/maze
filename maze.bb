
Graphics 640,480


Global grelha,fundo1,jogador,blocoexit,blocolimite,tiroimg
Global monster,vidasimg,timerframe
Global x,y
Global passo,frame,sair,startlevel
Global jogX,jogY,exitx,exity,outx,outy
Global direcao,tiroX,tiroY,tiro
Global nivel
Global timer,tempo,vidas
Global SomMorte,musica


Const UP=2
Const DOWN=3
Const DIR=0
Const ESQ=1

Const LARGURA=1000
Const ALTURA=1000

AutoMidHandle True

HidePointer()

LoadObjects()


SetBuffer BackBuffer()

Type TGrelha
	Field x,y
End Type

Type TEnimigos
	Field x,y
	Field direcao
	Field passo
End Type



; **************** LOADING ****************
; apenas se justifica este loading no carregamente da musica

loading=LoadImage("pics\loading.png")
Cls
DrawImage loading,320,150
Flip
musica=LoadSound("music\level1.ogg")
FreeImage loading

; **************** FIM LOADING *************

; *************** INIT VARS ******************
timer=100
nivel=1
vidas=5
sair=False
startlevel=False
SeedRnd MilliSecs()

; *************** FIM INIT ******************


; ************* PROGRAMA PRINCIPAL **************


menu()
If Not sair Then
	InitLevel(nivel)
	LoopSound musica 
	PlaySound musica
	Repeat
		Delay(25)
		Cls
		If startlevel Then
			InitLevel(nivel)
			startlevel=False
		End If
		Desenhar()
		Mover()
		If VerificarFimLevel() Then
			nivel=nivel+1
			If Not nivel=6 InitLevel(nivel)
			timer=100
		End If
		VerMorte()
		aux=MilliSecs()
		If aux-tempo>1000 Then
			timer=timer-1
			tempo=aux
		End If
	
		Flip
		If KeyHit(1) sair=True
		If nivel=6 sair=True
		If vidas=0 sair=True
		If timer=0 sair=True
	Until sair

	FreeMen()

	FlushKeys()

; ecra gameover
	Fim()
	
End If


; ************* FIM PROGRAMA PRINCIPAL **************

; *************  MENU ***************
Function menu()

fundointro=LoadImage("pics\cave.png")
letrasintro=LoadImage("pics\pergaminho.png")
seta=LoadImage("pics\seta.png")

titulo=LoadFont("Arial",32,True)
menu=LoadFont("Arial",24,True)
id=LoadFont("Arial",20,True)
som=LoadSound ("sons\coracao.wav")
LoopSound som
PlaySound som


escolha=1
sairmenu=False


Repeat
	Repeat
		Cls
		DrawImage fundointro,320,240
		DrawImage letrasintro,320,240
		DrawImage seta,200,180+escolha*30
		SetFont titulo
		Color 255,0,0
		Text 320,50,"SUPER MAZE",True
		SetFont id
		Text 320,440,"ADVANCED CYBERNETIC SYSTEMS - 2006",True
		SetFont menu
		Color 0,0,0
		Text 320,150,"MENU",True
		Text 320,200,"START",True
		Text 320,230,"CHOSE LEVEL",True
		Text 320,260,"HELP",True
		Text 320,290,"EXIT",True
		If (KeyHit(208) And escolha<4) escolha=escolha+1
		If (KeyHit(200) And escolha>1) escolha=escolha-1	
		Flip
	Until KeyHit(28)
	If escolha=1 Then	 ; sair do menu e comecar a jogar
		level=1
		sairmenu=True
	End If		
	If escolha=2 Then	; escolher level e comecar a jogar
		nivel=1
		sairmenu=True
		Repeat
			Cls
			DrawImage fundointro,320,240
			DrawImage letrasintro,320,240
			DrawImage seta,200,180+nivel*30
			SetFont titulo
			Color 255,0,0
			Text 320,50,"SUPER MAZE",True
			SetFont id
			Text 320,440,"ADVANCED CYBERNETIC SYSTEMS - 2006",True
			SetFont menu
			Color 0,0,0
			Text 320,150,"MENU",True
			Text 320,200,"NIVEL 1",True
			Text 320,230,"NIVEL 2",True
			Text 320,260,"NIVEL 3",True
			Text 320,290,"NIVEL 4",True
			Text 320,320,"NIVEL 5",True
			If (KeyHit(208) And nivel<5) nivel=nivel+1
			If (KeyHit(200) And nivel>1) nivel=nivel-1	
			Flip
		Until KeyHit(28)	
	End If
	If escolha=3 Then
		Repeat
			Cls
			DrawImage fundointro,320,240
			DrawImage letrasintro,320,240
			SetFont titulo
			Color 255,0,0
			Text 320,50,"SUPER MAZE",True
			SetFont id
			Text 320,440,"ADVANCED CYBERNETIC SYSTEMS - 2006",True
			SetFont menu
			Color 0,0,0
			Text 320,170,"KEYS - ARROWS",True
			Text 320,200,"PRESS ESC TO EXIT GAME",True
			Text 320,230,"5 LEVELS",True
			Text 320,260,"FIND THE EXIT",True
			Text 320,300,"PRESS ENTER TO CONT",True
			Flip
		Until KeyHit(28)		
	End If
	If escolha=4 Then
		sair=True
		sairmenu=True
	End If
Until sairmenu	
	

FreeImage letrasintro
FreeImage fundointro
FreeImage seta
FreeSound som

End Function



; *************  MENU END ***************




Function InitLevel(a)
	If a=1 Restore nivel1
	If a=2 Restore nivel2
	If a=3 Restore nivel3
	If a=4 Restore nivel4
	If a=5 Restore nivel5

	Read mapa
	y=mapa
	Read mapa
	x=mapa
	Read mapa
	passo=mapa
	
	; apagar blocos e monstros existentes
	For blocos.TGrelha=Each TGrelha
		Delete blocos
	Next
	
	For monstros.TEnimigos=Each TEnimigos
		Delete monstros
	Next
	

	For i=1 To 20
		For k=1 To 20
			Read mapa
			If mapa=0 Then
				blocos.TGrelha=New TGrelha
				blocos\x=x+k*50
				blocos\y=y+i*50
			End If
			If mapa=3 Then
				exitx=x+k*50
				exity=y+i*50
			End If
			If mapa=4 Then
				monstros.TEnimigos=New TEnimigos
				monstros\x=x+k*50
				monstros\y=y+i*50
				monstros\passo=Rand(5,passo)
				monstros\direcao=ESQ
			End If
			If mapa=5 Then
				monstros.TEnimigos=New TEnimigos
				monstros\x=x+k*50
				monstros\y=y+i*50
				monstros\passo=Rand(5,passo)
				monstros\direcao=UP
			End If

		Next
	Next
	
	Read mapa
	jogX=mapa
	Read mapa
	jogY=mapa
	Read mapa
	outx=mapa
	Read mapa
	outy=mapa
	
	Read mapa
	frame=mapa
	
	tiro=False
	tempo=MilliSecs()
	
	; carregar fonte e cor para timer
	fntArialB=LoadFont("Arial",32,True)
	SetFont fntArialB
	Color 255,100,100
	
End Function


Function LoadObjects()
	grelha=LoadImage("pics\bloco.jpg")
	jogador=LoadAnimImage ("pics\jogador.bmp",40,40,0,4)
	blocoexit=LoadImage("pics\blocoexit.jpg")
	blocolimite=LoadImage("pics\blocolimit.jpg")
	tiroimg=LoadImage ("pics\tiro.jpg")
	monster=LoadImage("pics\enimigo.png")
	vidasimg=LoadImage("pics\vidas.png")
	timerframe=LoadImage("pics\timerframe.png")
	fundo1=LoadImage("pics\fundo1.jpg")
	SomMorte=LoadSound ("sons\grito.wav")


End Function



Function Desenhar()
	TileImage fundo1,x,y
	For i=1 To 22
		DrawImage blocolimite,x-50+i*50,y-50
		DrawImage blocolimite,x-50+i*50,y+ALTURA+50
		DrawImage blocolimite,x,y-100+i*50
		DrawImage blocolimite,x+LARGURA+50,y-100+i*50
	Next
	
	For blocos.TGrelha=Each TGrelha
		DrawImage grelha,blocos\x,blocos\y
	Next
	
	; desenhar timer
	DrawImage timerframe,335,465
	Text 320,450,Str(timer)
	
	DrawImage jogador,jogX,jogY,frame
	
	For monstros.TEnimigos=Each TEnimigos
		DrawImage monster,monstros\x,monstros\y
	Next
	

	
	If tiro DrawImage tiroimg,tiroX,tiroY
	
	DrawImage blocoexit,exitx,exity
	

	;desenhar vidas
	For i=1 To vidas
		DrawImage vidasimg,20+i*32,460
	Next
	
	
End Function




Function Mover()
	If (KeyDown (203) And valido(ESQ)) Then
		For blocos.TGrelha=Each TGrelha
			blocos\x=blocos\x+passo
		Next
		For monstros.TEnimigos=Each TEnimigos
			monstros\x=monstros\x+passo
		Next
		x=x+passo
		exitx=exitx+passo
		If Not tiro direcao=ESQ
		frame=0
	End If 
	If (KeyDown (205) And valido(DIR)) Then
		x=x-passo
		exitx=exitx-passo
		If Not tiro direcao=DIR
		frame=1
		For blocos.TGrelha=Each TGrelha
			blocos\x=blocos\x-passo
		Next
		For monstros.TEnimigos=Each TEnimigos
			monstros\x=monstros\x-passo
		Next
	End If
	If (KeyDown (200) And valido(UP)) Then
		y=y+passo
		exity=exity+passo
		If Not tiro direcao=UP
		frame=2
		For blocos.TGrelha=Each TGrelha
			blocos\y=blocos\y+passo
		Next
		For monstros.TEnimigos=Each TEnimigos
			monstros\y=monstros\y+passo
		Next
	End If
	If (KeyDown (208) And valido(DOWN)) Then
		y=y-passo
		exity=exity-passo
		If Not tiro direcao=DOWN
		frame=3
		For blocos.TGrelha=Each TGrelha
			blocos\y=blocos\y-passo
		Next
		For monstros.TEnimigos=Each TEnimigos
			monstros\y=monstros\y-passo
		Next
	End If
	If (KeyHit(57) And tiro=False) Then
		tiro=True
		tiroX=jogX
		tiroY=jogY
	End If
	If tiro Then
		If direcao=ESQ tiroX=tiroX-passo*2
		If direcao=DIR tiroX=tiroX+passo*2
		If direcao=UP tiroY=tiroY-passo*2
		If direcao=DOWN tiroY=tiroY+passo*2
		For blocos.TGrelha=Each TGrelha
			If ImagesOverlap (grelha,blocos\x,blocos\y,tiroimg,tiroX,tiroY) tiro=False
		Next
	End If
	
	; mover enimigos
	For monstros.TEnimigos=Each TEnimigos
		If monstros\direcao=UP Then
			monstros\y=monstros\y-monstros\passo
			For blocos.TGrelha=Each TGrelha
				If ImagesOverlap (grelha,blocos\x,blocos\y+5,monster,monstros\x,monstros\y) monstros\direcao=DOWN
			Next
		End If
		If monstros\direcao=DOWN Then
			monstros\y=monstros\y+monstros\passo
			For blocos.TGrelha=Each TGrelha
				If ImagesOverlap (grelha,blocos\x,blocos\y-5,monster,monstros\x,monstros\y) monstros\direcao=UP
			Next		
		End If
		If monstros\direcao=ESQ Then
			monstros\x=monstros\x-monstros\passo
			For blocos.TGrelha=Each TGrelha
				If ImagesOverlap (grelha,blocos\x+5,blocos\y,monster,monstros\x,monstros\y) monstros\direcao=DIR
			Next	
		End If
		If monstros\direcao=DIR Then
			monstros\x=monstros\x+monstros\passo
			For blocos.TGrelha=Each TGrelha
				If ImagesOverlap (grelha,blocos\x-5,blocos\y,monster,monstros\x,monstros\y) monstros\direcao=ESQ
			Next	
		End If	
	Next	


End Function


Function FreeMen()
	FreeImage grelha
	FreeImage jogador
	FreeImage blocoexit
	FreeImage blocolimite
	FreeImage tiroimg
	FreeImage monster
	FreeImage vidasimg
	FreeImage timerframe
	FreeSound SomMorte
	FreeSound musica
End Function




Function valido(a)
	If a=UP Then
		For blocos.TGrelha=Each TGrelha
			If ImagesOverlap (grelha,blocos\x,blocos\y+15,jogador,jogX,jogY) Return 0
		Next
	End If
	If a=DOWN Then
		For blocos.TGrelha=Each TGrelha
			If ImagesOverlap (grelha,blocos\x,blocos\y-15,jogador,jogX,jogY) Return 0
		Next
	End If
	If a=ESQ Then
		For blocos.TGrelha=Each TGrelha
			If ImagesOverlap (grelha,blocos\x+15,blocos\y,jogador,jogX,jogY) Return 0
		Next
	End If
	If a=DIR Then
		For blocos.TGrelha=Each TGrelha
			If ImagesOverlap (grelha,blocos\x-15,blocos\y,jogador,jogX,jogY) Return 0
		Next
	End If
	Return 1
End Function


Function VerificarFimLevel()
	If ImagesOverlap (blocoexit,exitx+outx,exity+outy,jogador,jogX,jogY) Return 1
End Function


Function VerMorte()
	For monstros.TEnimigos=Each TEnimigos
		If ImagesCollide (monster,monstros\x,monstros\y,0,jogador,jogX,jogY,0) Then
			vidas=vidas-1
			startlevel=True
			PlaySound SomMorte
		End If
	Next
End Function



Function Fim()
	gameover=LoadImage("pics\gameover.png")
	Cls
	DrawImage gameover,320,240
	Flip
	WaitKey()
	FreeImage gameover
End Function



.nivel1
Data -260,-180,10
Data 2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,2
Data 2,2,0,0,0,0,0,0,0,0,0,0,0,0,2,0,1,1,1,0
Data 2,0,1,1,4,1,1,1,1,1,1,1,1,1,0,0,1,0,1,0
Data 2,0,1,0,0,0,0,0,1,0,0,0,0,1,1,1,1,0,1,0
Data 2,0,1,0,2,2,2,0,1,0,2,2,1,0,0,0,0,0,1,0
Data 2,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,2
Data 2,0,1,4,1,1,0,0,1,0,1,1,1,4,1,1,1,1,1,0
Data 0,0,0,0,0,1,0,0,1,0,1,0,0,1,0,0,0,0,1,0
Data 0,1,1,1,1,1,0,0,1,0,1,0,0,1,0,2,2,1,1,0
Data 0,0,1,0,0,0,2,0,1,0,1,0,0,1,0,0,0,0,1,0
Data 2,0,1,0,2,2,2,0,1,1,1,0,0,5,1,1,1,1,1,0
Data 2,0,1,0,2,2,2,0,0,1,0,2,0,1,0,1,0,0,1,0
Data 2,0,1,0,0,0,0,0,0,1,0,0,0,1,0,1,0,0,1,0
Data 2,0,1,1,1,4,1,1,1,1,1,1,1,1,0,1,0,0,1,0
Data 2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0
Data 2,0,1,1,1,1,1,1,1,1,1,1,4,1,1,1,1,1,1,0
Data 2,0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0
Data 2,0,1,1,1,1,0,2,0,1,0,0,0,0,0,0,0,2,2,2
Data 2,2,0,0,0,1,0,2,0,1,1,1,1,1,1,1,1,0,2,2
Data 2,2,2,2,0,3,0,2,0,0,0,0,0,0,0,0,0,2,2,2
Data 320,290,0,40,3


.nivel2
Data -260,-180,10
Data 2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0
Data 2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0
Data 2,0,1,1,1,4,1,1,1,1,1,1,1,1,1,1,1,1,0,2
Data 2,0,1,0,0,0,0,0,0,0,1,0,1,0,0,0,0,1,0,0
Data 2,0,1,1,1,1,1,1,1,1,1,0,1,0,2,2,0,1,0,0
Data 2,0,1,0,0,0,0,0,0,0,0,0,1,0,2,0,0,1,1,0
Data 2,0,1,0,0,0,0,0,0,0,2,0,1,0,0,0,0,5,0,0
Data 2,0,1,1,1,1,1,1,1,0,2,0,1,1,1,4,1,1,0,0
Data 2,2,0,0,0,0,0,0,1,0,2,0,1,0,0,0,0,1,0,0
Data 2,2,2,2,2,2,2,0,1,0,2,0,5,0,2,2,0,1,1,0
Data 2,0,0,0,0,0,0,0,0,0,0,0,1,0,2,2,2,0,1,0
Data 0,1,1,1,1,1,1,1,4,1,1,1,1,0,2,0,0,0,1,0
Data 0,5,0,0,0,1,0,0,0,1,0,0,1,0,0,1,1,4,1,0
Data 0,1,0,2,0,1,0,0,0,0,0,0,1,0,0,1,0,1,0,2
Data 0,1,0,0,0,1,1,1,1,0,0,0,1,0,0,1,0,1,0,2
Data 0,1,1,1,1,0,0,0,0,1,0,0,1,1,1,1,0,1,0,2
Data 0,1,0,0,1,0,2,2,0,1,0,0,0,0,0,0,0,1,0,2
Data 0,1,0,0,1,0,0,0,0,0,0,0,3,0,0,0,0,1,0,2
Data 0,1,1,4,1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,2
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2
Data 270,240,0,-40,2



.nivel3
Data -260,-180,10
Data 2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,1,1,4,1,1,1,1,1,1,1,3,4,1,1,1,1,1,1,0
Data 0,1,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,1,0
Data 0,1,0,2,0,1,1,1,1,1,1,1,1,1,0,2,2,0,1,0
Data 0,1,0,2,2,0,0,0,0,0,0,0,0,1,0,2,2,0,1,0
Data 0,0,2,2,2,2,2,2,2,2,2,2,0,1,0,0,0,0,1,0
Data 2,2,2,2,2,2,2,2,2,2,2,2,0,1,1,1,4,1,1,0
Data 0,0,0,0,0,0,0,0,0,2,2,2,0,1,0,0,0,0,1,0
Data 0,1,1,1,1,1,1,1,1,0,2,2,0,1,0,2,2,0,1,0
Data 0,1,0,0,0,1,0,0,1,0,2,2,0,1,0,2,2,0,1,0
Data 0,1,0,2,0,1,0,0,1,0,2,2,0,1,1,0,2,0,5,0
Data 0,1,0,2,0,1,0,0,1,0,2,2,2,0,1,0,2,0,1,0
Data 0,1,0,2,0,0,0,0,5,0,0,0,0,0,5,0,2,0,1,0
Data 2,0,2,2,0,0,1,0,1,1,1,4,1,1,1,0,2,0,1,0
Data 2,0,0,0,0,0,1,0,1,0,0,0,0,0,1,0,2,0,1,0
Data 0,1,1,1,1,1,1,1,1,0,2,2,2,0,1,0,2,0,1,0
Data 0,1,0,0,0,0,0,0,0,2,2,2,2,0,1,0,2,0,1,0
Data 0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1,0
Data 0,1,1,1,1,1,1,1,4,1,1,1,1,1,1,1,1,1,1,0
Data 2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2
Data 270,240,0,-40,2

.nivel4
Data -260,-180,10
Data 2,2,2,2,2,2,2,2,0,2,0,0,0,0,0,0,0,0,0,2
Data 2,0,0,0,2,2,2,0,1,0,1,1,1,1,1,4,1,1,1,0
Data 0,1,1,1,0,2,2,0,1,0,1,0,0,1,0,0,1,0,0,2
Data 2,0,0,1,0,2,2,0,1,0,1,0,0,3,0,0,1,0,2,2
Data 2,2,0,1,0,2,2,0,1,1,1,0,0,5,0,0,1,0,2,2
Data 2,2,0,1,0,0,0,0,1,0,0,0,0,1,0,0,1,0,2,2
Data 2,2,0,1,1,1,1,1,5,1,1,1,1,1,0,0,1,0,2,2
Data 2,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,2
Data 0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,4,1,1,1,0
Data 2,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,2
Data 2,0,1,0,2,2,2,0,1,0,0,0,1,1,1,1,1,1,1,0
Data 2,0,1,0,2,2,2,0,1,0,0,1,1,0,0,0,0,0,1,0
Data 2,0,1,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,1,0
Data 2,0,1,1,1,1,1,1,4,1,1,1,1,1,1,1,1,0,1,0
Data 2,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,5,0
Data 2,0,1,0,2,2,0,0,5,0,2,2,2,0,0,0,1,0,1,0
Data 2,0,1,0,2,0,1,1,1,0,2,2,0,1,1,1,1,0,1,0
Data 2,0,1,0,0,0,1,0,1,0,2,2,0,1,0,0,0,0,1,0
Data 0,1,1,4,1,1,1,0,1,0,2,2,0,1,1,1,4,1,1,0
Data 2,0,0,0,0,0,0,2,0,2,2,2,2,0,0,0,0,0,0,0
Data 270,240,0,-40,2


.nivel5
Data -260,-180,10
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 0,1,1,4,1,1,0,1,1,4,1,1,0,1,1,1,1,1,1,0
Data 0,1,0,0,0,1,0,1,0,1,0,1,0,1,0,0,0,0,1,0
Data 0,5,0,0,0,1,0,1,0,5,0,1,1,5,0,0,0,0,1,0
Data 0,1,0,0,0,1,0,1,0,1,0,1,0,1,0,0,0,0,1,0
Data 0,1,0,0,0,1,1,1,0,1,0,1,0,1,1,1,1,0,5,0
Data 0,1,0,0,0,1,0,1,0,1,0,5,0,1,0,0,1,0,1,0
Data 0,1,0,0,0,1,0,1,0,1,0,1,0,1,0,0,1,0,1,0
Data 0,1,0,0,0,1,0,1,0,1,0,1,0,1,0,0,1,0,1,0
Data 0,1,0,0,0,1,0,1,1,1,1,1,0,1,0,0,1,0,1,0
Data 0,5,1,1,0,1,0,1,0,0,0,1,1,1,0,0,1,0,1,0
Data 0,1,0,1,0,5,0,1,0,1,0,1,0,1,0,0,1,0,1,0
Data 0,1,0,1,0,1,0,1,0,5,0,1,0,1,0,0,1,4,1,0
Data 0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,0,1,0
Data 0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,0,1,0
Data 0,1,0,1,0,1,0,1,0,1,0,1,1,1,0,3,0,0,1,0
Data 0,1,0,1,1,1,1,1,1,1,0,5,0,1,0,1,0,0,1,0
Data 0,1,0,0,0,1,0,1,0,0,0,1,0,1,0,1,0,0,1,0
Data 0,1,1,4,1,1,0,1,1,4,1,1,0,1,1,1,4,1,1,0
Data 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
Data 270,240,0,-40,2
