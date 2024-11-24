import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Input } from './ui/input';
import { Button } from './ui/button';

// Interfejs dla danych użytkownika
interface UserData {
    age: number;
    weight: number;
    height: number;
    gender: 'MALE' | 'FEMALE';
    activityLevel: 'SEDENTARY' | 'LIGHTLY_ACTIVE' | 'MODERATELY_ACTIVE' | 'VERY_ACTIVE' | 'EXTRA_ACTIVE';
    goal: Goal;
}

// Interfejs dla wyników zapotrzebowania
interface DailyRequirements {
    calories: number;
    protein: number;
    carbs: number;
    fat: number;
}

// Typowanie propsów komponentu
interface SettingsProps {
    userId: number;
    onSettingsUpdate: (requirements: DailyRequirements) => void;
}

enum Goal {
    LOSE_WEIGHT = 'LOSE_WEIGHT',
    MAINTAIN_WEIGHT = 'MAINTAIN_WEIGHT',
    GAIN_WEIGHT = 'GAIN_WEIGHT'
}

// Dodaj enum ActivityLevel
enum ActivityLevel {
    SEDENTARY = 'SEDENTARY',
    LIGHTLY_ACTIVE = 'LIGHTLY_ACTIVE',
    MODERATELY_ACTIVE = 'MODERATELY_ACTIVE',
    VERY_ACTIVE = 'VERY_ACTIVE',
    EXTRA_ACTIVE = 'EXTRA_ACTIVE'
}

const Settings: React.FC<SettingsProps> = ({ userId, onSettingsUpdate }) => {
    // Stan do przechowywania danych użytkownika
    const [userData, setUserData] = useState<UserData>({
        age: 0,
        weight: 0,
        height: 0,
        gender: 'MALE',
        activityLevel: 'SEDENTARY',
        goal: Goal.MAINTAIN_WEIGHT,
    });

    // Stan na zapotrzebowanie kaloryczne i makroskładniki
    const [dailyRequirements, setDailyRequirements] = useState<DailyRequirements | null>(null);

    // Obsługa zmiany danych w formularzu
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setUserData({
            ...userData,
            [name]: name === "age" || name === "weight" || name === "height" ? Number(value) : value
        } as UserData);
    };

    // Funkcja obsługująca przesłanie danych i wyliczenie zapotrzebowania
    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        
        try {
            console.log('Wysyłam dane:', userData); // debugging
            
            const response = await axios.post<DailyRequirements>(
                `http://localhost:8081/api/users/${userId}/calculate-daily-requirement`,
                userData,
                {
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                }
            );

            console.log('Odpowiedź z API:', response.data);

            if (response.data) {
                const requirements: DailyRequirements = {
                    calories: response.data.calories || 0,
                    protein: response.data.protein || 0,
                    carbs: response.data.carbs || 0,
                    fat: response.data.fat || 0
                };

                onSettingsUpdate(requirements);
            }
        } catch (error) {
            if (axios.isAxiosError(error)) {
                console.error("Błąd podczas obliczania zapotrzebowania:", {
                    status: error.response?.status,
                    data: error.response?.data,
                    headers: error.response?.headers
                });
            } else {
                console.error("Nieznany błąd:", error);
            }
        }
    };

    useEffect(() => {
        try {
            axios.get(`http://localhost:8081/api/users/${userId}/daily-requirements`, {
                headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        }).then(response => {
            console.log('Current goals:', response.data); // debugging
            setDailyRequirements(response.data);
            });
        } catch (error) {
            console.error("Błąd podczas pobierania zapotrzebowania:", error);
        }
    }, []);

    return (
        <div className="p-4 max-w-3xl mx-auto">
            <form onSubmit={handleSubmit} className="space-y-6">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="space-y-2">
                        <label className="text-sm font-medium text-gray-700">Wiek:</label>
                        <Input 
                            type="number" 
                            name="age" 
                            value={userData.age || ''} 
                            onChange={handleChange} 
                            required 
                            className="w-full"
                            min="0"
                            max="150"
                        />
                    </div>
                    <div className="space-y-2">
                        <label className="text-sm font-medium text-gray-700">Waga (kg):</label>
                        <Input 
                            type="number" 
                            name="weight" 
                            value={userData.weight || ''} 
                            onChange={handleChange} 
                            required 
                            className="w-full"
                            min="0"
                            max="300"
                        />
                    </div>
                    <div className="space-y-2">
                        <label className="text-sm font-medium text-gray-700">Wzrost (cm):</label>
                        <Input 
                            type="number" 
                            name="height" 
                            value={userData.height || ''} 
                            onChange={handleChange} 
                            required 
                            className="w-full"
                            min="0"
                            max="300"
                        />
                    </div>
                    <div className="space-y-2">
                        <label className="text-sm font-medium text-gray-700">Płeć:</label>
                        <select 
                            name="gender" 
                            value={userData.gender} 
                            onChange={handleChange} 
                            className="w-full rounded-md border border-gray-300 p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value="MALE">Mężczyzna</option>
                            <option value="FEMALE">Kobieta</option>
                        </select>
                    </div>
                    <div className="space-y-2">
                        <label className="text-sm font-medium text-gray-700">Poziom aktywności:</label>
                        <select 
                            name="activityLevel" 
                            value={userData.activityLevel} 
                            onChange={handleChange} 
                            className="w-full rounded-md border border-gray-300 p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value={ActivityLevel.SEDENTARY}>Siedzący (brak aktywności)</option>
                            <option value={ActivityLevel.LIGHTLY_ACTIVE}>Lekko aktywny (1-3 treningi/tydzień)</option>
                            <option value={ActivityLevel.MODERATELY_ACTIVE}>Umiarkowanie aktywny (3-5 treningów/tydzień)</option>
                            <option value={ActivityLevel.VERY_ACTIVE}>Bardzo aktywny (6-7 treningów/tydzień)</option>
                            <option value={ActivityLevel.EXTRA_ACTIVE}>Super aktywny (2x dziennie treningi)</option>
                        </select>
                    </div>
                    <div className="space-y-2">
                        <label className="text-sm font-medium text-gray-700">Cel:</label>
                        <select 
                            name="goal" 
                            value={userData.goal} 
                            onChange={handleChange} 
                            className="w-full rounded-md border border-gray-300 p-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
                        >
                            <option value={Goal.LOSE_WEIGHT}>Redukcja masy ciała</option>
                            <option value={Goal.MAINTAIN_WEIGHT}>Utrzymanie masy ciała</option>
                            <option value={Goal.GAIN_WEIGHT}>Zwiększenie masy ciała</option>
                        </select>
                    </div>
                </div>
                
                <Button type="submit" className="w-full bg-blue-600 hover:bg-blue-700">
                    Przelicz zapotrzebowanie
                </Button>
            </form>
            
            {dailyRequirements && (
                <div className="mt-6 p-6 bg-white rounded-lg shadow-sm border border-gray-200">
                    <h3 className="text-lg font-semibold mb-4">Twoje zapotrzebowanie</h3>
                    <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                        <div className="p-4 bg-gray-50 rounded-lg text-center">
                            <p className="text-sm text-gray-600">Kalorie</p>
                            <p className="text-xl font-bold">{dailyRequirements.calories} kcal</p>
                        </div>
                        <div className="p-4 bg-gray-50 rounded-lg text-center">
                            <p className="text-sm text-gray-600">Białko</p>
                            <p className="text-xl font-bold">{dailyRequirements.protein} g</p>
                        </div>
                        <div className="p-4 bg-gray-50 rounded-lg text-center">
                            <p className="text-sm text-gray-600">Węglowodany</p>
                            <p className="text-xl font-bold">{dailyRequirements.carbs} g</p>
                        </div>
                        <div className="p-4 bg-gray-50 rounded-lg text-center">
                            <p className="text-sm text-gray-600">Tłuszcze</p>
                            <p className="text-xl font-bold">{dailyRequirements.fat} g</p>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Settings;
